package com.mkxrs.ollama_api.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.mkxrs.ollama_api.dto.Message;

import org.springframework.stereotype.Service;

//@Service
//commenting @service cz now we are gonna use redisconversationstor. For switching back to in memory just uncomment this
public class InMemoryConversationStore implements ConversationStore{

	private final Map<String,List<Message>> store=new ConcurrentHashMap<>();
	
	@Override 
	public List<Message> get(String ConversationId)
	{
		return store.computeIfAbsent(ConversationId,id->java.util.Collections.synchronizedList(new ArrayList<>()));
	}
	@Override
	public void save(String conversationId, List<Message> history)
	{
		
		store.put(conversationId, history);
		
	}
}
