package com.crimson_code_blog_rest_apis.dto.request;

public class EmailVerificationRequest {

	private String email;
	
	public EmailVerificationRequest () {
		
	}

	public EmailVerificationRequest(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
