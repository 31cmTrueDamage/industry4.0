package org.industry40.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MachineClient {

    private final WebClient webClient;

    public MachineClient(WebClient.Builder webClientBuilder,
                         @Value("${machines.service.url:http://localhost:8097}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void allocateMachine(Integer machineId, LocalDateTime until) {
        log.info("Execution: Allocating machine #{} until {}", machineId, until);

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/machines/{id}/allocate")
                        .queryParam("until", until.toString())
                        .build(machineId))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void releaseMachine(Integer machineId) {
        log.info("Execution: Releasing machine #{}", machineId);

        this.webClient.post()
                .uri("/machines/{id}/release", machineId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}