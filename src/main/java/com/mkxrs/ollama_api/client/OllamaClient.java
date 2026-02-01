package com.mkxrs.ollama_api.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
//import java.util.ArrayList;
import com.mkxrs.ollama_api.dto.Message;

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

    public String chat(List<Message> history) {

        Map<String, Object> body = Map.of(
            "model", model,
            "messages", history,
            "stream", false
        );

        Map response = webClient.post()
            .uri("/api/chat")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Map.class)
            .block();
        Map message=(Map) response.get("message");
        
        return (String) message.get("content");
    }
}
