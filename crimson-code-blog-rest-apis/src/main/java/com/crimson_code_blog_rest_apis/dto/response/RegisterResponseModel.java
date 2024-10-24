package com.crimson_code_blog_rest_apis.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

public class RegisterResponseModel {

	private String publicId;

	private String email;

	private String firstName;

	private String lastName;
	
	private String joinedAt;
	
	public RegisterResponseModel() {
		
	}

	public RegisterResponseModel(String publicId, String email,
			String firstName, String lastName, String joinedAt) {
		this.publicId = publicId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.joinedAt = joinedAt;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicUserId) {
		this.publicId = publicUserId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(String joinedAt) {
		this.joinedAt = joinedAt;
	}
	
}
