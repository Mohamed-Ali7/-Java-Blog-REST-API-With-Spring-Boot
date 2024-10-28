package com.crimson_code_blog_rest_apis.dto.request;

public class PasswordResetRequestModel {
	private String email;

	public PasswordResetRequestModel() {

	}

	public PasswordResetRequestModel(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
