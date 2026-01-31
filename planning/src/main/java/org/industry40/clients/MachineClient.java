package org.industry40.clients;

import org.industry40.dtos.MachineDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MachineClient {

    private final WebClient webClient;

    public MachineClient(WebClient.Builder webClientBuilder,
                         @Value("${machines.service.url:http://localhost:8097}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public List<MachineDTO> getAllMachines() {
        return webClient.get()
                .uri("/machines")
                .retrieve()
                .bodyToFlux(MachineDTO.class)
                .collectList()
                .block();
    }
}