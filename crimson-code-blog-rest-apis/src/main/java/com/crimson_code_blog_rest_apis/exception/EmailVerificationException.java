package com.crimson_code_blog_rest_apis.exception;

public class EmailVerificationException extends RuntimeException {
	public EmailVerificationException(String message) {
		super(message);
	}
}
