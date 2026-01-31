package org.industry40.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String PLAN_READY_QUEUE = "plan_ready_queue";
    public static final String PLAN_READY_EXCHANGE = "plan_ready_exchange";
    public static final String PLAN_READY_ROUTING_KEY = "plan_ready_routing_key";

    public static final String ORDER_CANCEL_QUEUE = "order_cancel_queue";
    public static final String ORDER_CANCEL_EXCHANGE = "order_cancel_exchange";
    public static final String ORDER_CANCEL_ROUTING_KEY = "order_cancel_routing_key";

    public static final String NOTIFICATION_QUEUE = "notification_queue";
    public static final String NOTIFICATION_EXCHANGE = "notification_exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification_routing_key";

    public static final String ORDER_STATUS_SYNC_QUEUE = "order_status_sync_queue";
    public static final String ORDER_STATUS_SYNC_EXCHANGE = "order_status_sync_exchange";
    public static final String ORDER_STATUS_SYNC_ROUTING_KEY = "order_status_sync_routing_key";

    public static final String NOTIFICATION_CANCEL_QUEUE = "notification_cancel_queue";
    public static final String NOTIFICATION_CANCEL_ROUTING_KEY = "notification_cancel_routing_key";

    public static final String ORDER_COMPLETED_SYNC_QUEUE = "order_completed_sync_queue";
    public static final String ORDER_COMPLETED_SYNC_EXCHANGE = "order_completed_sync_exchange";
    public static final String ORDER_COMPLETED_SYNC_ROUTING_KEY = "order_completed_sync_routing_key";

    @Bean
    public TopicExchange planReadyExchange() {
        return new TopicExchange(PLAN_READY_EXCHANGE);
    }

    @Bean
    public Queue planReadyQueue() {
        return QueueBuilder.durable(PLAN_READY_QUEUE).build();
    }

    @Bean
    public Binding planReadyBinding(Queue planReadyQueue, TopicExchange planReadyExchange) {
        return BindingBuilder.bind(planReadyQueue).to(planReadyExchange).with(PLAN_READY_ROUTING_KEY);
    }

    @Bean
    public TopicExchange orderCancelExchange() {
        return new TopicExchange(ORDER_CANCEL_EXCHANGE);
    }

    @Bean
    public Queue orderCancelQueue() {
        return QueueBuilder.durable(ORDER_CANCEL_QUEUE).build();
    }

    @Bean
    public Binding orderCancelBinding(Queue orderCancelQueue, TopicExchange orderCancelExchange) {
        return BindingBuilder.bind(orderCancelQueue).to(orderCancelExchange).with(ORDER_CANCEL_ROUTING_KEY);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public TopicExchange orderStatusSyncExchange() {
        return new TopicExchange(ORDER_STATUS_SYNC_EXCHANGE);
    }

    @Bean
    public Queue orderStatusSyncQueue() {
        return QueueBuilder.durable(ORDER_STATUS_SYNC_QUEUE).build();
    }

    @Bean
    public Binding orderStatusSyncBinding(Queue orderStatusSyncQueue, TopicExchange orderStatusSyncExchange) {
        return BindingBuilder.bind(orderStatusSyncQueue).to(orderStatusSyncExchange).with(ORDER_STATUS_SYNC_ROUTING_KEY);
    }

    @Bean
    public Queue notificationCancelQueue() {
        return QueueBuilder.durable(NOTIFICATION_CANCEL_QUEUE).build();
    }

    @Bean
    public Binding notificationCancelBinding(Queue notificationCancelQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationCancelQueue).to(notificationExchange).with(NOTIFICATION_CANCEL_ROUTING_KEY);
    }

    @Bean
    public TopicExchange orderCompletedSyncExchange() {
        return new TopicExchange(ORDER_COMPLETED_SYNC_EXCHANGE);
    }

    @Bean
    public Queue orderCompletedSyncQueue() {
        return QueueBuilder.durable(ORDER_COMPLETED_SYNC_QUEUE).build();
    }

    @Bean
    public Binding orderCompletedSyncBinding() {
        return BindingBuilder.bind(orderCompletedSyncQueue())
                .to(orderCompletedSyncExchange())
                .with(ORDER_COMPLETED_SYNC_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
