package org.industry40.clients;

import org.industry40.dtos.ItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CheckInventoryClient {

    private final WebClient webClient;

    public CheckInventoryClient(WebClient.Builder builder,
                                @Value("${inventory.service.url:http://localhost:8093}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public ItemDTO getStock(Integer productId) {
        return webClient.get()
                .uri("/inventory/{itemId}", productId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new RuntimeException("Product Not Found in Inventory: " + productId)))
                .bodyToMono(ItemDTO.class)
                .block();
    }
}