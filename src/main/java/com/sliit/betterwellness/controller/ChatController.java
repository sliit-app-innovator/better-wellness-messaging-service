package com.sliit.betterwellness.controller;

import com.sliit.betterwellness.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	private final Map<String, List<ChatMessage>> chatHistory = new HashMap<>();

	public ChatController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}


	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		log.info("Received message {}", chatMessage);
		// Dynamic topic based on customerId and counsellorId
		String topic = String.format("/topic/chat/%d-%d", chatMessage.getCustomerId(), chatMessage.getCounsellorId());
		addChatHostory(chatMessage, topic);
		messagingTemplate.convertAndSend(topic, chatMessage);
	}

	@GetMapping("/history")
	public List<ChatMessage> getChatHistory(@RequestParam int customerId, @RequestParam int counsellorId) {
		String topic = String.format("/topic/chat/%d-%d", customerId, counsellorId);
		return chatHistory.getOrDefault(topic, new ArrayList<>());
	}

	private void addChatHostory(ChatMessage chatMessage, String topic) {
		if (!this.chatHistory.containsKey(topic)) {
			chatHistory.put(topic, new ArrayList<>());
		}
		chatHistory.get(topic).add(chatMessage);
	}
}