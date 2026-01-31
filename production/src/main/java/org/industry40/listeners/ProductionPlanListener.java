package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.JobRepository;
import org.industry40.enums.JobStatus;
import org.industry40.messages.OrderCancelMessage;
import org.industry40.messages.ProductionPlanMessage;
import org.industry40.models.ProductionJob;
import org.industry40.services.ProductionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductionPlanListener {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProductionService productionService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = MQConfig.PLAN_READY_QUEUE)
    public void listener(ProductionPlanMessage message) {
        log.info("Execution Service: Received Plan #{} for Order #{}", message.getPlanId(), message.getOrderId());

        try {
            ProductionJob job = new ProductionJob();
            job.setPlanId(message.getPlanId());
            job.setUserId(message.getUserId());
            job.setOrderId(message.getOrderId());
            job.setProductId(message.getProductId());
            job.setMachineIds(message.getMachineIds());
            job.setStartTime(message.getScheduledStartTime());
            job.setEndTime(message.getScheduledStartTime().plusMinutes(message.getDurationInMinutes()));
            job.setStatus(JobStatus.PENDING);
            jobRepository.save(job);

            productionService.scheduleJob(job);

            log.info("Execution Service: Job #{} saved and ready for execution at {}",
                    job.getPlanId(), job.getStartTime());
        } catch (Exception e) {
            log.error("Execution Service: Failed to process plan #{}: {}", message.getPlanId(), e.getMessage());
        }
    }

    @RabbitListener(queues = MQConfig.ORDER_CANCEL_QUEUE)
    public void cancelListener(OrderCancelMessage message) {
        log.info("Execution Service: Received cancellation request for Order #{}", message.getOrderId());

        try {
            List<ProductionJob> jobs = jobRepository.findByOrderId(message.getOrderId());

            if (jobs.isEmpty()) {
                log.warn("No jobs found for Order #{}", message.getOrderId());
                return;
            }

            for (ProductionJob job : jobs) {
                productionService.cancelJob(job.getPlanId());
            }


            rabbitTemplate.convertAndSend(
                    MQConfig.NOTIFICATION_EXCHANGE,
                    MQConfig.NOTIFICATION_CANCEL_ROUTING_KEY,
                    message
            );

            rabbitTemplate.convertAndSend(
                    MQConfig.ORDER_STATUS_SYNC_EXCHANGE,
                    MQConfig.ORDER_STATUS_SYNC_ROUTING_KEY,
                    message
            );

            log.info("Execution Service: Order #{} cancellation synced with Notifications and Orders", message.getOrderId());

        } catch (IllegalStateException e) {
            log.error("Execution Service: Cannot cancel Order #{} because a job is already in progress.", message.getOrderId());
        } catch (Exception e) {
            log.error("Execution Service: Failed to process cancellation for order #{}: {}", message.getOrderId(), e.getMessage());
        }
    }


}
