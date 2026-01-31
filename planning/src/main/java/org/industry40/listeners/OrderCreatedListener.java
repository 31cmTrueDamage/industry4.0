package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.PlanRepository;
import org.industry40.dtos.ScheduleResultDTO;
import org.industry40.enums.PlanStatus;
import org.industry40.messages.OrderCreatedMessage;
import org.industry40.messages.ProductionPlanMessage;
import org.industry40.models.ProductionPlan;
import org.industry40.services.PlanningService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedListener {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private RabbitTemplate template;

    @RabbitListener(queues = MQConfig.PLANNING_ORDER_CREATED_QUEUE)
    public void listener(OrderCreatedMessage message) {
        log.info("Planning Service: Processing Order #{} for Product ID {}", message.getOrderId(), message.getProductId());

        try {
            ScheduleResultDTO result = planningService.calculateSchedule(message.getQuantity());

            ProductionPlan plan = new ProductionPlan();
            plan.setOrderId(message.getOrderId());
            plan.setProductId(message.getProductId());
            plan.setQuantity(message.getQuantity());
            plan.setUserId(message.getUserId());

            plan.setAssignedMachineIds(result.getMachineIds());
            plan.setScheduledStartTime(result.getStartTime());

            plan.setStatus(PlanStatus.SCHEDULED);

            planRepository.save(plan);

            ProductionPlanMessage execMessage = new ProductionPlanMessage(
                    plan.getId(),
                    plan.getOrderId(),
                    plan.getUserId(),
                    plan.getProductId(),
                    plan.getQuantity(),
                    plan.getAssignedMachineIds(),
                    plan.getScheduledStartTime(),
                    (long) (plan.getQuantity() * 0.5)
            );

            template.convertAndSend(MQConfig.PLAN_READY_EXCHANGE, MQConfig.PLAN_READY_ROUTING_KEY, execMessage);

            log.info("Production Plan created successfully for Order #{} starting at {}",
                    message.getOrderId(), result.getStartTime());

        } catch (Exception e) {
            log.error("Error creating production plan for order {}: {}", message.getOrderId(), e.getMessage());
        }
    }
}
