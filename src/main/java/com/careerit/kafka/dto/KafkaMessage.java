package com.careerit.kafka.dto;

public class KafkaMessage {
	private String message;

	public KafkaMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KafkaMessage(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "KafkaMessage [message=" + message + "]";
	}
	

}
