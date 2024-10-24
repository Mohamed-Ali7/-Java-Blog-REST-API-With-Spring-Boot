package com.crimson_code_blog_rest_apis.service.impl;

import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private DateTimeFormatter dateFormatter;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
			DateTimeFormatter dateFormatter) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.dateFormatter = dateFormatter;
	}
	

	@Override
	public UserResponseModel getUser(long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + id));
		
		for (RoleEntity role : user.getRoles()) {
			System.out.println(role.getName());
		}
		
		String formattedDate = dateFormatter.format(user.getJoinedAt());

		UserResponseModel userResponse = modelMapper.map(user, UserResponseModel.class);

		userResponse.setJoinedAt(formattedDate);
		return userResponse;
	}

}
