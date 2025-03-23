package com.sliit.betterwellness.controller;

import com.sliit.betterwellness.config.SessionRegistry;
import com.sliit.betterwellness.dto.ChatMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisMessageSubscriber implements MessageListener {

	private final SessionRegistry sessionRegistry;
	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;

	public RedisMessageSubscriber(SessionRegistry sessionRegistry, SimpMessagingTemplate messagingTemplate) {
		this.sessionRegistry = sessionRegistry;
		this.messagingTemplate = messagingTemplate;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String body = new String(message.getBody());
			ChatMessage chatMessage = objectMapper.readValue(body, ChatMessage.class);
			String topic = String.format("/topic/chat/%d-%d", chatMessage.getCustomerId(), chatMessage.getCounsellorId());

			// Send only to sessions connected to THIS pod
			if (ChatController.getActiveSessionTopic().containsValue(topic)) {
				System.out.println("Bradcasting message to topic "+ topic);
				messagingTemplate.convertAndSend(topic, chatMessage);
				messagingTemplate.convertAndSend(topic, chatMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
