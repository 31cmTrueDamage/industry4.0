package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.ItemRepository;
import org.industry40.messages.ProductCreatedMessage;
import org.industry40.messages.ProductSyncMessage;
import org.industry40.models.Item;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCreatedListener {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = MQConfig.PRODUCT_CREATED_QUEUE)
    public void listener(ProductCreatedMessage message) {
        itemRepository.findById(message.getProductId()).ifPresentOrElse(
                existingItem -> {
                    log.info("Item {} already exists, updating stock instead.", message.getProductId());
                    existingItem.setQuantity(message.getStock());
                    itemRepository.save(existingItem);
                },
                () -> {
                    log.info("Creating new inventory item for product {}", message.getProductId());
                    Item newItem = new Item();
                    newItem.setId(message.getProductId());
                    newItem.setQuantity(message.getStock());
                    itemRepository.save(newItem);
                }
        );

        ProductSyncMessage syncMessage = new ProductSyncMessage(message.getMessageId());
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE_SYNC, MQConfig.ROUTING_KEY_SYNC, syncMessage);
    }
}
