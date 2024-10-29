package com.crimson_code_blog_rest_apis.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequestModel {

	private String currentPassword;

	@NotNull(message = "Password cannot be Empty.")
	@Size(min = 6, max = 30, message = "Password must be 6 to 30 characters long.")
	private String newPassword;
	private String confirmPassword;
	
	public ChangePasswordRequestModel() {
		
	}

	public ChangePasswordRequestModel(String oldPassword, String newPassword, String confirmNewPassword) {
		this.currentPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmNewPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String oldPassword) {
		this.currentPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmNewPassword) {
		this.confirmPassword = confirmNewPassword;
	}
	
}
