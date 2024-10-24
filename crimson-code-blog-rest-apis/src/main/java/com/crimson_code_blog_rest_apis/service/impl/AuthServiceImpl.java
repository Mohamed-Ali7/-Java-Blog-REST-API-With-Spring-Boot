package com.crimson_code_blog_rest_apis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.RoleRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.service.AuthService;
import com.crimson_code_blog_rest_apis.service.EmailService;
import com.crimson_code_blog_rest_apis.utils.UserRoles;

@Service
public class AuthServiceImpl implements AuthService {
	
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private DateTimeFormatter dateFormatter;
	private EmailService emailService;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
			DateTimeFormatter dateFormatter, EmailService emailService,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.dateFormatter = dateFormatter;
		this.emailService = emailService;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public RegisterResponseModel register(RegisterRequestModel registerRequestModel) {
		
		if (userRepository.findByEmail(registerRequestModel.getEmail()).isPresent()) {
			throw new RuntimeException("This email already exists");
		}
		
		UserEntity newUser = modelMapper.map(registerRequestModel, UserEntity.class);
		
		newUser.setPublicId(UUID.randomUUID().toString());
		newUser.setJoinedAt(LocalDateTime.now());
		
		RoleEntity userRole = roleRepository.findByName(UserRoles.ROLE_USER.name())
				.orElseGet(() -> {
					RoleEntity roleUser = new RoleEntity(UserRoles.ROLE_USER.name());
					return roleRepository.save(roleUser);
				});
		
		String emailVerificationToken = UUID.randomUUID().toString();
		newUser.setEmailVerificationToken(emailVerificationToken);

		newUser.addRole(userRole);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		
		UserEntity savedUser = userRepository.save(newUser);

		String formattedDate = dateFormatter.format(savedUser.getJoinedAt());

		RegisterResponseModel registerResponse = modelMapper.map(savedUser,
													RegisterResponseModel.class);

		registerResponse.setJoinedAt(formattedDate);
		
		emailService.sendVerificationEmail(savedUser.getEmail(), emailVerificationToken);

		return registerResponse;
	}

	@Override
	public void emailVerification(String verificationToken) {

		UserEntity user = userRepository.findByEmailVerificationToken(verificationToken)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid email verification token"));
		
		user.setIsUserVerified(true);
		user.setEmailVerificationToken(null);
		
		userRepository.save(user);
	}

}
