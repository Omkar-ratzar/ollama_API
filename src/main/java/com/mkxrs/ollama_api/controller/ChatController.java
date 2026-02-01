package com.mkxrs.ollama_api.controller;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mkxrs.ollama_api.dto.ChatRequest;
import com.mkxrs.ollama_api.service.ChatService;




@RestController
public class ChatController {
	 private final ChatService chatService;

	    public ChatController(ChatService chatService) {
	        this.chatService = chatService;
	    }
	

	//using @GetMapping to mention the URL route of /chat, hardcoding the result as of now. 
//		@GetMapping("/chat")
		@PostMapping("/chat")
		public String reply(@RequestBody ChatRequest request) {
		    long start = System.nanoTime();
		    String response = chatService.reply(
		            request.getMessage(),
		            request.getConversationId()
		    );
		    long end = System.nanoTime();
		    long durationMs = (end - start) / 1_000_000;

		    System.out.println("response time / length of output: " + durationMs/response.length() + " ms/characters"); 
		    //avg 600 to 670ms on ollama meta llama3.2. OK DAMN, avg 2ms/character. 
		    //here I am calculating time after the response is generated
		    /*NEW KNOWLEDGE :D --> LLM backend APIs require additional performance metrics because the response is constantly generating, streaming and token based
		     * TTFT p95 and p99 are 3 practices used for testing the performance of a LLM based API
		     * TTFT stands for Time to first token i.e. instead of waiting to complete, while the LLM is streaming, it will check when the LLM responded with the first token
		     * The p in p95 and p99 stand for percentile
		     * In p95 you check for the slowest 5% of requests, it ensures that most of the users are fine with the delivery
		     * in p99 you check for the slowest 1% of requests, it checks for worst case dmg, while testing many times initially the first request was having response time of 3 to 5 SECONDS which is huge, the reason was I am using ollama as of now, the GPU has to get on work and ollama has to start this LLM which will take a little more time, this is also called cold start. p99 helps u determine operational risk i.e. captures these cold start effects while using avg latency alone*/

		    return response;
		}

}
