package com.crimson_code_blog_rest_apis.service;

public interface EmailService {
	void sendVerificationEmail(String username, String token);
	void sendPasswordResetEmail(String username, String token);
}
