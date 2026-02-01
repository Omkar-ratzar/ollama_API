package com.mkxrs.ollama_api.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.mkxrs.ollama_api.dto.Message;
import java.time.Duration;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.mkxrs.ollama_api.store.ConversationStore;

@Service
public class RedisConversationStore implements ConversationStore {
	
	//configuring stuff we need to use for redis masala
	private final StringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;
	
	public RedisConversationStore(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
	    this.redisTemplate = redisTemplate;
	    this.objectMapper = objectMapper;//object mapper is necessary as the data from redis is in bytes/strings
	}
	//def. key and TTL
	//defining key is necessary because thats how we gonna store this in redis in a key value pair, here the key prefix for conversationID is necessary cz as of now we do not have a proper namespace configuration for conversationIds 
	//so it might add some collisions and in redis it'd be easier to operate bulk tasks like DEL prefix.* so it'd delete all prefixed values, it'd be dangerous if we just do DEL *. would be crezy.
	//good practice while using redis hmm :)
	private static final String KEY_PREFIX = "conversation:";
	private static final Duration TTL = Duration.ofMinutes(30);
	private String key(String conversationId) {
	    return KEY_PREFIX + conversationId;
	}


	
	//implementing methods from conversationStore down here.
	@Override
	public List<Message> get(String conversationId) {
		// TODO Auto-generated method stub
		String json = redisTemplate.opsForValue().get(key(conversationId)); //getting the json object from redis db for that conversationID
	    if (json == null) {//if the json object is null i.e. if the conversationID isnt present redis will just return null for the value of that key. 
	        return new ArrayList<>();//using an arraylist instead of synchronized arraylist cz redis itself eliminates shared in-JVM state, so synchronization is no longer required. Redis promises atomicity and isolation. 
	    }
	    //technically now is the else part so if the conversationId exists then, 
	    try {
	        return objectMapper.readValue(
	            json,
	            new TypeReference<List<Message>>() {}//typeReference is necessary cz mapping has to be done properly so objectmapper does not know what typa list we are fetching so it'd be unable to convert the list properly to a java object so we use type reference to state that this list is of type Message
	        );
	    } catch (Exception e) {
	        throw new RuntimeException("[-] ERROR: Failed to deserialize conversation", e);
	    }
		
	}

	@Override
	public void save(String conversationId, List<Message> history) {
		// TODO Auto-generated method stub
		try {
	        String json = objectMapper.writeValueAsString(history);//turns the whole list to a json key value pair form
	        redisTemplate.opsForValue().set(key(conversationId), json, TTL);
	    } catch (Exception e) {
	        throw new RuntimeException("[-] ERROR: Failed to serialize conversation", e);
	    }
		
	}

}
