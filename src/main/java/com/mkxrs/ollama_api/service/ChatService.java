package com.mkxrs.ollama_api.service;

import org.springframework.stereotype.Service;

import com.mkxrs.ollama_api.client.OllamaClient;

@Service
public class ChatService {
	
	private final OllamaClient ollamaClient;

    public ChatService(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }
	
    
	public String reply(String message)
	{
		//hardcoding this now, will add functionality later on. 
//		return "REPLY FROM 'reply' in ChatService.java,input="+message;
		return ollamaClient.generate_reply(message);
		
		
	}
}
