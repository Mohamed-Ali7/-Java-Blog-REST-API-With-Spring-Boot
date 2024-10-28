package com.crimson_code_blog_rest_apis.exception;

import java.util.Date;

public class ErrorResponse {
	private int statusCode;
	private String message;
	private Date timestamp;
	
	public ErrorResponse() {
		
	}

	public ErrorResponse(int statusCode, String message, Date timestamp) {
		this.statusCode = statusCode;
		this.message = message;
		this.timestamp = timestamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
