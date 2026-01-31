package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.NotificationRepository;
import org.industry40.data.OrderProgressRepository;
import org.industry40.messages.JobFinishedMessage;
import org.industry40.messages.OrderCancelMessage;
import org.industry40.models.Notification;
import org.industry40.models.OrderProgress;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductionListener {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private OrderProgressRepository orderProgressRepository;

    @RabbitListener(queues = MQConfig.NOTIFICATION_QUEUE)
    public void handleJobFinished(JobFinishedMessage msg) {
        log.info("Received JobFinished for Order #{})", msg.getOrderId());

        OrderProgress progress = orderProgressRepository.findById(msg.getOrderId())
                .orElse(new OrderProgress(msg.getOrderId(), 0));

        progress.setCompletedCount(progress.getCompletedCount() + 1);
        orderProgressRepository.save(progress);

        log.info("Order #{} Progress: {}/{}", msg.getOrderId(), progress.getCompletedCount(), msg.getTotalItemsInOrder());

        if (progress.getCompletedCount() >= msg.getTotalItemsInOrder()) {
            sendFinalNotification(msg.getUserId(), msg.getOrderId());

            orderProgressRepository.delete(progress);
        }
    }

    @RabbitListener(queues = MQConfig.NOTIFICATION_CANCEL_QUEUE)
    public void handleOrderCancelled(OrderCancelMessage msg) {
        log.info("Received Cancellation Confirmation for Order #{}", msg.getOrderId());

        Notification notification = new Notification();
        notification.setUserId(msg.getUserId());
        notification.setOrderId(msg.getOrderId());
        notification.setTitle("Order Cancelled");
        notification.setMessage("Your order #" + msg.getOrderId() + " was successfully cancelled and production stopped.");
        notification.setType("CANCELLED");

        notificationRepository.save(notification);

        orderProgressRepository.findById(msg.getOrderId())
                .ifPresent(progress -> orderProgressRepository.delete(progress));

        log.info("CANCEL NOTIFICATION stored for Order #{}", msg.getOrderId());
    }

    private void sendFinalNotification(Integer userId, Integer orderId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setOrderId(orderId);
        notification.setTitle("Order Finished");
        notification.setMessage("Production for order:  #" + orderId + " concluded.");
        notification.setType("SUCCESS");

        notificationRepository.save(notification);
        log.info("FINAL NOTIFICATION sent and stored for Order #{}", orderId);
    }
}
