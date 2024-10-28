package com.crimson_code_blog_rest_apis.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PasswordResetConfirmRequest {

	private String token;
	
	@NotNull(message = "Password cannot be Empty.")
	@Size(min = 6, max = 30, message = "Password must be 6 to 30 characters long.")
	private String newPassword;
	private String confirmNewPassword;
	
	public PasswordResetConfirmRequest() {
		
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String repeatNewPassword) {
		this.confirmNewPassword = repeatNewPassword;
	}
	
	
}
