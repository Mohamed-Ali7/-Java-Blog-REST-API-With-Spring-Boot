package com.crimson_code_blog_rest_apis.service;

import java.util.List;

import com.crimson_code_blog_rest_apis.dto.request.ChangePasswordRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;

public interface UserService {
	UserResponseModel getUser(String publicId);

	void passwordResetRequest(PasswordResetRequestModel passwordResetReuest);
	
	void resetPassword(PasswordResetConfirmRequest passwordResetConfirm);

	List<UserResponseModel> getAllUsers(int page, int pageSize, String sortBy);

	UserResponseModel updateUser(String publicId, UpdateUserRequestModel updateRequest);

	void deleteUser(String publicId);

	void changePassword(String publicId, ChangePasswordRequestModel changePasswordRequest);
	
	List<PostResponseModel> getUserPosts(String publicId, int page, int pageSize, String sortBy);
	
}
