package com.crimson_code_blog_rest_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.service.AuthService;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.utils.OperationName;
import com.crimson_code_blog_rest_apis.utils.OperationStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

	AuthService authService;
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseModel> postMethodName(
			@RequestBody RegisterRequestModel registerRequest) {
		
		return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
	}

	@GetMapping("/email-verification")
	public ResponseEntity<OperationStatusResponse> emailVerification(
					@RequestParam(value = "token") String verificationToken){
		
		OperationStatusResponse operationResponse = new OperationStatusResponse();
		
		operationResponse.setOperationName(OperationName.VERIFIY_USER_EMAIL.name());
		operationResponse.setOperationStatus(OperationStatus.SUCCESS.name());
		operationResponse.setMessage("Email verified successfully");
		
		authService.emailVerification(verificationToken);
		
		return new ResponseEntity<>(operationResponse, HttpStatus.OK);
	}	
}
