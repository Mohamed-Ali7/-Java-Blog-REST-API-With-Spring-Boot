package com.crimson_code_blog_rest_apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/users/{id}")
	public UserResponseModel getUser(@PathVariable long id) {
		return userService.getUser(id);
	}
}
