package com.crimson_code_blog_rest_apis.service;

import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;

public interface UserService {
	UserResponseModel getUser(long id);
}
