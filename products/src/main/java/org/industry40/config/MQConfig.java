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

    // PRODUCT IS CREATED, TRY TO SYNC
    public static final String PRODUCT_CREATED_QUEUE = "product_created_queue";
    public static final String EXCHANGE = "product_created_exchange";
    public static final String ROUTING_KEY = "product_created_routing_key";

    // PRODUCT IS SYNCED, FINALIZE
    public static final String PRODUCT_SYNC_QUEUE = "product_sync_queue";
    public static final String EXCHANGE_SYNC = "product_sync_exchange";
    public static final String ROUTING_KEY_SYNC = "product_sync_routing_key";

    @Bean
    public Queue creationQueue() {
        return QueueBuilder.durable(PRODUCT_CREATED_QUEUE).build();
    }

    @Bean
    public TopicExchange creationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding creationBinding(Queue creationQueue, TopicExchange creationExchange) {
        return BindingBuilder.bind(creationQueue).to(creationExchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue syncQueue() {
        return QueueBuilder.durable(PRODUCT_SYNC_QUEUE).build();
    }

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(EXCHANGE_SYNC);
    }

    @Bean
    public Binding syncBinding(Queue syncQueue, TopicExchange syncExchange) {
        return BindingBuilder.bind(syncQueue).to(syncExchange).with(ROUTING_KEY_SYNC);
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
