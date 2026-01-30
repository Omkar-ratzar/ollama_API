package com.mkxrs.ollama_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mkxrs.ollama_api.service.ChatService;




@RestController
public class ChatController {
	 private final ChatService chatService;

	    public ChatController(ChatService chatService) {
	        this.chatService = chatService;
	    }
	
	
	//using @GetMapping to mention the URL route of /chat, hardcoding the result as of now. 
		@GetMapping("/chat")
		public String reply(@RequestParam(required=false) String input ) {
		      //return String.format("HARDCODED ENDPOINT WORKING");
			if(input==null||input.isBlank())
				return String.format("OYE KUCH TOH LIKH DE INPUT ME OYE!");
			else
				return chatService.reply(input);
			
		      
		    }
}
