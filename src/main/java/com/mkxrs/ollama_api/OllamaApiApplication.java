package com.mkxrs.ollama_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
//will use @RequestParam later if needed
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication

public class OllamaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OllamaApiApplication.class, args);
	}
	

}

