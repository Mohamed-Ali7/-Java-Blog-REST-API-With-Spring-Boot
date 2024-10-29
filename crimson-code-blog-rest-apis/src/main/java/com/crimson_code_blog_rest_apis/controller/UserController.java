package com.crimson_code_blog_rest_apis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.ChangePasswordRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
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

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public List<UserResponseModel> getAllUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize,
			@RequestParam(value = "sort", defaultValue = "id") String sortBy
			){
		
		return userService.getAllUsers(page, pageSize, sortBy);
	}
	
	@GetMapping("/{publicId}")
	public  UserResponseModel getUser(@PathVariable String publicId) {
		return userService.getUser(publicId);
	}
	
	@PreAuthorize("hasRole('USER') and principal.publicId == #publicId")
	@PutMapping("/{publicId}")
	public UserResponseModel updateUser(@PathVariable String publicId,
											@RequestBody UpdateUserRequestModel updateRequest) {
		return userService.updateUser(publicId, updateRequest);
	}
	
	@PreAuthorize("hasRole('ADMIN') or principal.publicId == #publicId")
	@DeleteMapping("/{publicId}")
	public OperationStatusResponse deleteUser(@PathVariable String publicId) {
		
		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		userService.deleteUser(publicId);
		
		operationStatus.setOperationName(OperationName.DELETE_USER.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("The user has been deleted successfully");
		
		return operationStatus;
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
	
	@PreAuthorize("hasRole('USER') and principal.publicId == #publicId")
	@PutMapping("/{publicId}/edit/password")
	public OperationStatusResponse changePassword(@PathVariable String publicId,
						@RequestBody ChangePasswordRequestModel changePasswordRequest) {

		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		userService.changePassword(publicId, changePasswordRequest);
		
		operationStatus.setOperationName(OperationName.CHANGE_PASSWORD.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("Your password has been successfully updated.");
		return operationStatus;
	}
	
	@GetMapping("/{publicId}/posts")
	public List<PostResponseModel> getUserPosts(@PathVariable String publicId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize,
			@RequestParam(value = "sort", defaultValue = "id") String sortBy) {

		return userService.getUserPosts(publicId, page, pageSize, sortBy);
	}
	
}
