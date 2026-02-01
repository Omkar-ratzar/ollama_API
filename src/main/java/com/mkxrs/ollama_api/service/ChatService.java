package com.mkxrs.ollama_api.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.HashMap;

import com.mkxrs.ollama_api.client.OllamaClient;
import com.mkxrs.ollama_api.dto.Message;
import com.mkxrs.ollama_api.store.ConversationStore;



@Service
public class ChatService {
	
	private final ConversationStore conversationStore; 
	private final OllamaClient ollamaClient;
	public ChatService(OllamaClient ollamaClient, ConversationStore conversationStore) 
	{ 
		this.ollamaClient = ollamaClient; 
		this.conversationStore = conversationStore; 
	}
	
    
//    private List<Message> history=new ArrayList<>();
    
//    private final Map<String, List<Message>> conversations = new ConcurrentHashMap<>();
//	commenting this out now cz now we're shifting to redis :D 
    //using concurrenthashmap cz in production when multiple requests hit at once, hashmap fails and concurrent hashmap is made for multithreading applications
    
    private void add_message(String role, String content, List<Message> history)
    {
    	Message msg=new Message();
    	msg.setRole(role);
    	msg.setContent(content);
    	history.add(msg);
    }
    
	public String reply(String message,String conversationId)
	{
		if(conversationId==null||conversationId.isEmpty()||conversationId.isBlank())
			return "[-] ERROR: IN STRING REPLY. FAILED TO FETCH CONVERSATIONID. FIX JSON OBJECT";
		
/*		
		List<Message>history;
		if(!conversations.containsKey(conversationId))
		{
			 history=new ArrayList<>();
			conversations.put(conversationId, history);
		}
		else {
			history=conversations.get(conversationId);
		}
*/
//		DELETING ABOVE PART BECAUSE IT IS NOT ATOMIC, WILL CAUSE PROBELMS IN PRODUCTION. 
		
//		List<Message> history = conversations.computeIfAbsent(conversationId, id -> new ArrayList<>());
//		List<Message> history = conversations.computeIfAbsent(conversationId, id -> java.util.Collections.synchronizedList(new ArrayList<>()));
		List<Message> history = conversationStore.get(conversationId);

		//hardcoding this now, will add functionality later on. 
//		return "REPLY FROM 'reply' in ChatService.java,input="+message;
		String answer;
		synchronized (history) {
		if(history.isEmpty()) //adding the system message in list only if it is empty i.e. only the first time, or else it just increases redudnancy. 
			add_message("system","You are cold hearted, straight forward, highly motivating, commander. You reply only in 3-4 sentences to every text",history);
		
		int MAX_LIST_SIZE=12;
		
		if(history.size()>=MAX_LIST_SIZE)
			while(history.size()>MAX_LIST_SIZE+1)
			history.remove(1);
		add_message("user",message,history);

		}
		answer=ollamaClient.chat(history);
		
		synchronized (history) {
		add_message("assistant",answer,history);
		}
		conversationStore.save(conversationId, history);
		return answer;
		
	}
}
