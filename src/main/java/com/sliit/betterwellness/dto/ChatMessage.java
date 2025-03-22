package com.sliit.betterwellness.dto;

public class ChatMessage {

	private String sender;
	private String message;
	private int customerId;
	private int counsellorId;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getCounsellorId() {
		return counsellorId;
	}

	public void setCounsellorId(int counsellorId) {
		this.counsellorId = counsellorId;
	}
}

