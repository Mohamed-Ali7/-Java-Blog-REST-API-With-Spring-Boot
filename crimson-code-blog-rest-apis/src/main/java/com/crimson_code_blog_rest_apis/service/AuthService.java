package com.crimson_code_blog_rest_apis.service;

import com.crimson_code_blog_rest_apis.dto.request.EmailVerificationRequest;
import com.crimson_code_blog_rest_apis.dto.request.LoginRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.LogoutRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.LoginResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.RefreshTokenResponse;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
	RegisterResponseModel register(RegisterRequestModel registerRequestModel);

	void emailVerification(String verificationToken);

	LoginResponseModel login(LoginRequestModel loginRequest);

	void emailVerificationRequest(EmailVerificationRequest verificationRequest);

	RefreshTokenResponse refreshAccessToken(HttpServletRequest request);

	void logout(LogoutRequestModel logoutRequest, HttpServletRequest request);
}
