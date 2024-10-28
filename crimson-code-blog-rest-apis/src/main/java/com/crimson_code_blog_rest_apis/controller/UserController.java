package com.crimson_code_blog_rest_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.service.UserService;
import com.crimson_code_blog_rest_apis.utils.OperationName;
import com.crimson_code_blog_rest_apis.utils.OperationStatus;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/{publicId}")
	public  UserResponseModel getUser(@PathVariable String publicId) {
		return userService.getUser(publicId);
	}
	
	@PostMapping("/password-reset-request")
	public OperationStatusResponse passwordResetRequest(
			@RequestBody() PasswordResetRequestModel passwordResetReuest) {
		
		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		userService.passwordResetRequest(passwordResetReuest);
		
		operationStatus.setOperationName(OperationName.PASSWORD_RESET_REQUEST.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("Password reset link has sent to your email addess successfully");
		
		return operationStatus;
	}
	
	@PostMapping("/password-reset")
	public OperationStatusResponse passwordResetConfirm(
			@RequestBody() PasswordResetConfirmRequest passwordResetConfirm) {
		
		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		userService.resetPassword(passwordResetConfirm);
		
		operationStatus.setOperationName(OperationName.PASSWORD_RESET_CONFIRM.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("Your password has been reset successfully.");
		
		return operationStatus;
	} 
}
