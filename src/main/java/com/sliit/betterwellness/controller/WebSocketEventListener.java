package com.sliit.betterwellness.controller;

import com.sliit.betterwellness.config.SessionRegistry;
import com.sliit.betterwellness.controller.ChatController;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

	private final SessionRegistry sessionRegistry;

	public WebSocketEventListener(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		ChatController.getActiveSessionTopic().putIfAbsent(sessionId, "NA");
		System.out.println("New WebSocket connection. SessionID: " + sessionId);
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		ChatController.getActiveSessionTopic().remove(sessionId);
		System.out.println("WebSocket Disconnected. SessionID: " + sessionId);
	}
}

