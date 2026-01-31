package org.industry40.clients;

import org.industry40.dtos.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CheckCustomerClient {

    private final WebClient webClient;

    public CheckCustomerClient(WebClient.Builder builder,
                               @Value("${customers.service.url:http://localhost:8091}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public void validateCustomer(Integer customerId) {
        webClient.get()
                .uri("/customers/{id}", customerId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new RuntimeException("Customer ID " + customerId + " does not exist!")))
                .bodyToMono(CustomerDTO.class)
                .block();
    }
}