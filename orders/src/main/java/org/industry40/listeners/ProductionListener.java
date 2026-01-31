package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.OrdersRepository;
import org.industry40.enums.OrderStatus;
import org.industry40.messages.JobFinishedMessage;
import org.industry40.messages.OrderCancelMessage;
import org.industry40.models.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductionListener {

    @Autowired
    private OrdersRepository ordersRepository;

    @RabbitListener(queues = MQConfig.ORDER_STATUS_SYNC_QUEUE)
    public void handleStatusSync(OrderCancelMessage msg) {
        log.info("Orders Service: Received cancel sync for Order #{}", msg.getOrderId());
        updateStatus(msg.getOrderId(), OrderStatus.CANCELLED);
    }

    @RabbitListener(queues = MQConfig.ORDER_COMPLETED_SYNC_QUEUE)
    public void handleOrderCompleted(JobFinishedMessage msg) {
        log.info("Orders Service: Received completion sync for Order #{}", msg.getOrderId());
        updateStatus(msg.getOrderId(), OrderStatus.COMPLETED);
    }

    private void updateStatus(Integer orderId, OrderStatus status) {
        try {
            Order order = ordersRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            if (order.getStatus() != status) {
                order.setStatus(status);
                ordersRepository.save(order);
                log.info("Orders Service: Order #{} status updated to {}", orderId, status);
            }
        } catch (Exception e) {
            log.error("Orders Service: Failed to update status for Order #{}: {}", orderId, e.getMessage());
        }
    }
}
