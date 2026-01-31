package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.ItemRepository;
import org.industry40.exceptions.NegativeStockException;
import org.industry40.messages.OrderCreatedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedListener {

    @Autowired
    private ItemRepository itemRepository;

    @RabbitListener(queues = MQConfig.INVENTORY_ORDER_QUEUE)
    public void listener(OrderCreatedMessage message) {
        itemRepository.findById(message.getProductId()).ifPresentOrElse(
                item -> {
                    log.info("Updating stock for Item ID: {}. Current: {}, Reducing by: {}",
                            item.getId(), item.getQuantity(), message.getQuantity());

                    int newQuantity = item.getQuantity() - message.getQuantity();

                    if (newQuantity < 0) {
                        log.error("Aborting stock update for Item {}: Request for {} exceeds current stock of {}",
                                item.getId(), message.getQuantity(), item.getQuantity());

                        throw new NegativeStockException("Stock cannot be negative for item: " + item.getId());
                    }

                    item.setQuantity(newQuantity);

                    itemRepository.save(item);
                    log.info("Stock updated successfully. New quantity: {}", newQuantity);
                },
                () -> log.error("CRITICAL: Received order for Product ID {} but it doesn't exist in Inventory DB!", message.getProductId())
        );
    }
}
