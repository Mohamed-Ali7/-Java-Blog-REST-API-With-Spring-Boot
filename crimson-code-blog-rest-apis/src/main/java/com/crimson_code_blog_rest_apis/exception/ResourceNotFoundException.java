package com.crimson_code_blog_rest_apis.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
