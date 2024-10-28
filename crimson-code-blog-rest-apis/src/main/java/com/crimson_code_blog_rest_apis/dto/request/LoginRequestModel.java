package com.crimson_code_blog_rest_apis.dto.request;

import jakarta.validation.constraints.NotNull;

public class LoginRequestModel {

	@NotNull(message = "Email cannot be Empty.")
	private String email;
	
	@NotNull(message = "Password cannot be Empty.")
	private String password;
	
	public LoginRequestModel() {
		
	}

	public LoginRequestModel(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
