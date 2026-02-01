package com.mkxrs.ollama_api.store;

import java.util.List;

import com.mkxrs.ollama_api.dto.Message;

public interface ConversationStore {
	List<Message> get(String conversationId); 
	void save(String conversationId, List<Message> history);
}
