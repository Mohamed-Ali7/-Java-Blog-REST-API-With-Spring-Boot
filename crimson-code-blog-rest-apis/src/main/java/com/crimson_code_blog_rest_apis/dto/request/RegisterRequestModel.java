package com.crimson_code_blog_rest_apis.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequestModel {
	
	@NotNull(message = "Email cannot be Empty.")
	@Email(message = "Email must be a well-formed, like : name@crimson-code.com.")
	private String email;
	
	@NotNull(message = "Password cannot be Empty.")
	@Size(min = 6, max = 30, message = "Password must be 6 to 30 characters long.")
	private String password;
	
	@NotNull(message = "First name cannot be Empty.")
	private String firstName;
	
	@NotNull(message = "Last name cannot be Empty.")
	private String lastName;
	
	public RegisterRequestModel() {
		
	}

	public RegisterRequestModel(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
