package com.mkxrs.ollama_api.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Service
public class OllamaClient {

    private final WebClient webClient;
    private final String model;

    public OllamaClient(
        WebClient.Builder builder,
        @Value("${ollama.base-url}") String baseUrl,
        @Value("${ollama.model}") String model
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.model = model;
    }

    public String generate_reply(String prompt) {

        Map<String, Object> body = Map.of(
            "model", model,
            "prompt", prompt,
            "stream", false
        );

        Map response = webClient.post()
            .uri("/api/generate")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        return (String) response.get("response");
    }
}
