package com.sliit.betterwellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class Message {
	public Message(String id, String content, Date createdAt) {
		this.id = id;
		this.content = content;
		this.createdAt = createdAt;
	}

	private String id;
	private String content;
	private Date createdAt;
}