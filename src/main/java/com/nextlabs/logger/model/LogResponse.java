package com.nextlabs.logger.model;

import org.springframework.http.HttpStatus;

/**
 * @author Sarita Sethy
 */
public class LogResponse {
	private HttpStatus status;
	private String message;

	public LogResponse(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
