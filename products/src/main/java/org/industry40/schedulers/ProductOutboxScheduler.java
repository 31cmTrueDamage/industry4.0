package org.industry40.schedulers;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.OutboxRepository;
import org.industry40.data.ProductsRepository;
import org.industry40.enums.ProductStatus;
import org.industry40.messages.ProductCreatedMessage;
import org.industry40.models.Product;
import org.industry40.models.ProductOutbox;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductOutboxScheduler {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void processPendingOutbox() {
        List<ProductOutbox> pendingMessages = outboxRepository.findByStatus(ProductStatus.PENDING);

        if (pendingMessages.isEmpty()) {
            return;
        }

        log.info("Found {} pending messages in outbox. Processing...", pendingMessages.size());

        for (ProductOutbox outbox : pendingMessages) {
            try {
                Product product = productsRepository.findById(outbox.getProductId()).orElse(null);
                if (product == null) {
                    log.error("Product ID {} not found. Marking outbox entry as FAILED.", outbox.getProductId());
                    continue;
                }

                ProductCreatedMessage message = new ProductCreatedMessage(
                        outbox.getId(),
                        product.getId(),
                        product.getStock()
                );

                rabbitTemplate.convertAndSend(
                        MQConfig.EXCHANGE,
                        MQConfig.ROUTING_KEY,
                        message
                );

            } catch (Exception e) {
                log.error("Failed to send Product {} to RabbitMQ. Will retry in 10 seconds. Error: {}",
                        outbox.getProductId(), e.getMessage());
            }
        }
    }
}
