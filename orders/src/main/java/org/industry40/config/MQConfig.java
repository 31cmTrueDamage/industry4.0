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

    public static final String EXCHANGE = "order_created_exchange";

    public static final String ROUTING_KEY = "order.created.*";

    public static final String INVENTORY_QUEUE = "inventory_order_queue";
    public static final String PLANNING_QUEUE = "planning_order_queue";

    public static final String ORDER_CANCEL_QUEUE = "order_cancel_queue";
    public static final String ORDER_CANCEL_EXCHANGE = "order_cancel_exchange";
    public static final String ORDER_CANCEL_ROUTING_KEY = "order_cancel_routing_key";

    public static final String ORDER_STATUS_SYNC_QUEUE = "order_status_sync_queue";
    public static final String ORDER_STATUS_SYNC_EXCHANGE = "order_status_sync_exchange";
    public static final String ORDER_STATUS_SYNC_ROUTING_KEY = "order_status_sync_routing_key";

    public static final String ORDER_COMPLETED_SYNC_QUEUE = "order_completed_sync_queue";
    public static final String ORDER_COMPLETED_SYNC_EXCHANGE = "order_completed_sync_exchange";
    public static final String ORDER_COMPLETED_SYNC_ROUTING_KEY = "order_completed_sync_routing_key";

    @Bean
    public TopicExchange creationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue inventoryQueue() {
        return QueueBuilder.durable(INVENTORY_QUEUE).build();
    }

    @Bean
    public Queue planningQueue() {
        return QueueBuilder.durable(PLANNING_QUEUE).build();
    }

    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, TopicExchange creationExchange) {
        return BindingBuilder.bind(inventoryQueue).to(creationExchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding planningBinding(Queue planningQueue, TopicExchange creationExchange) {
        return BindingBuilder.bind(planningQueue).to(creationExchange).with(ROUTING_KEY);
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