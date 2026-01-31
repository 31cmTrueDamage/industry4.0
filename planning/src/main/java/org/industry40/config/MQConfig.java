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

    public static final String PLANNING_ORDER_CREATED_QUEUE = "planning_order_queue";
    public static final String ORDER_EXCHANGE = "order_created_exchange";
    public static final String ORDER_ROUTING_KEY = "order_created_routing_key";

    public static final String PLAN_READY_QUEUE = "plan_ready_queue";
    public static final String PLAN_READY_EXCHANGE = "plan_ready_exchange";
    public static final String PLAN_READY_ROUTING_KEY = "plan_ready_routing_key";

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(PLANNING_ORDER_CREATED_QUEUE).build();
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

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
