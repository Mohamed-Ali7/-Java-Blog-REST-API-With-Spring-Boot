package com.crimson_code_blog_rest_apis.dto.response;

public class RefreshTokenResponse {
	
	private String token;

	public RefreshTokenResponse() {
		
	}
	
	public RefreshTokenResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
