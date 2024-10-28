package com.crimson_code_blog_rest_apis.service;

import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;

public interface UserService {
	UserResponseModel getUser(String publicId);

	void passwordResetRequest(PasswordResetRequestModel passwordResetReuest);
	
	void resetPassword(PasswordResetConfirmRequest passwordResetConfirm);
	
}
