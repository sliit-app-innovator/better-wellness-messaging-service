package com.sliit.betterwellness.controller;

import com.sliit.betterwellness.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RestController
@RequestMapping("/messaging-service")
//@CrossOrigin(origins = "*")
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, Object> redisTemplate;
	private final Map<String, List<ChatMessage>> chatHistory = new HashMap<>();
	private final static Map<String, String> activeSessionTopic = new ConcurrentHashMap<>();

	@Value("${chat.history.ttl}")
	private int chatHistoryTTL;

	public ChatController(SimpMessagingTemplate messagingTemplate, RedisTemplate<String, Object> redisTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.redisTemplate = redisTemplate;
	}


	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public void sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		log.info("Received message {}", chatMessage);
		// Dynamic topic based on customerId and counsellorId
		String topic = String.format("/topic/chat/%d-%d", chatMessage.getCustomerId(), chatMessage.getCounsellorId());
		addChatHostory(chatMessage, topic);

		ChatController.getActiveSessionTopic().putIfAbsent(headerAccessor.getSessionId(), "NA");
		ChatController.getActiveSessionTopic().put(headerAccessor.getSessionId(), topic);
		//messagingTemplate.convertAndSend(topic, chatMessage);
		// Publish to Redis
		redisTemplate.convertAndSend("chatChannel", chatMessage);
	}

	@GetMapping("/history")
	public List<ChatMessage> getChatHistory(@RequestParam int customerId, @RequestParam int counsellorId) {
		String topic = String.format("/topic/chat/%d-%d", customerId, counsellorId);
		String redisKey = "chat:history:" + topic;

		List<Object> rawMessages = redisTemplate.opsForList().range(redisKey, 0, -1);
		if (rawMessages == null || rawMessages.isEmpty()) {
			return  new ArrayList<>();
		} else {
			return rawMessages.stream()
					.filter(obj -> obj instanceof ChatMessage)
					.map(obj -> (ChatMessage) obj)
					.toList();
		}
	}

	private void addChatHostory(ChatMessage chatMessage, String topic) {
		String redisKey = "chat:history:" + topic;
		redisTemplate.opsForList().rightPush(redisKey, chatMessage);
		redisTemplate.expire(redisKey, chatHistoryTTL, TimeUnit.SECONDS);
	}


	public static Map<String, String> getActiveSessionTopic() {
		return activeSessionTopic;
	}
}