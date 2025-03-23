package com.sliit.betterwellness.config;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionRegistry {

	private final ConcurrentHashMap<String, List<String>> topicSessions = new ConcurrentHashMap<>();

	public void addSession(String sessionId, String topic) {
		topicSessions.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(sessionId);
	}

	public void removeSession(String sessionId) {
		topicSessions.forEach((topic, sessions) -> sessions.remove(sessionId));
	}

	public List<String> getSessions(String topic) {
		return topicSessions.getOrDefault(topic, new CopyOnWriteArrayList<>());
	}
}
