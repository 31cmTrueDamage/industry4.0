package org.industry40.listeners;

import lombok.extern.slf4j.Slf4j;
import org.industry40.config.MQConfig;
import org.industry40.data.OutboxRepository;
import org.industry40.enums.ProductStatus;
import org.industry40.messages.ProductSyncMessage;
import org.industry40.models.ProductOutbox;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryListener {

    @Autowired
    private OutboxRepository  outboxRepository;

    @RabbitListener(queues = MQConfig.PRODUCT_SYNC_QUEUE)
    public void syncListener(ProductSyncMessage message) {
        ProductOutbox outbox = outboxRepository.findById(message.getOutboxId()).orElse(null);
        if (outbox != null) {
            outbox.setStatus(ProductStatus.COMPLETED);
            outboxRepository.save(outbox);
            log.info("Synced");
        }

    }
}
