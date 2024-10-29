package com.crimson_code_blog_rest_apis.dto.request;

public class UpdateUserRequestModel {

	private String firstName;
	private String lastName;
	
	public UpdateUserRequestModel() {
		
	}

	public UpdateUserRequestModel(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
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
	
}
