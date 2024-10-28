package com.crimson_code_blog_rest_apis.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.entity.PasswordResetTokenEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.PasswordResetTokenRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.service.EmailService;
import com.crimson_code_blog_rest_apis.service.UserService;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private DateTimeFormatter dateFormatter;
	private JwtUtils jwtUtils;
	private EmailService emailService;
	private PasswordResetTokenRepository passwordResetTokenRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
			DateTimeFormatter dateFormatter, EmailService emailService, JwtUtils jwtUtils,
			PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.dateFormatter = dateFormatter;
		this.emailService = emailService;
		this.jwtUtils = jwtUtils;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
	}
	

	@Override
	public UserResponseModel getUser(String publicId) {
		UserEntity user = userRepository.findByPublicId(publicId)
				.orElseThrow(() ->
					new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		String formattedDate = dateFormatter.format(user.getJoinedAt());

		UserResponseModel userResponse = modelMapper.map(user, UserResponseModel.class);

		userResponse.setJoinedAt(formattedDate);
		return userResponse;
	}


	@Override
	public void passwordResetRequest(PasswordResetRequestModel passwordResetReuest) {
		
		String userEmail = passwordResetReuest.getEmail();
		
		UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> 
				new ResourceNotFoundException("User does not exist with email: " + userEmail));
		
		Optional <PasswordResetTokenEntity> resetTokenChecker =
					passwordResetTokenRepository.findByUserId(user.getId());
		
		if (resetTokenChecker.isPresent()) {
			PasswordResetTokenEntity oldPasswordResetToken = resetTokenChecker.get();
			user.setPasswordResetToken(null);
			passwordResetTokenRepository.delete(oldPasswordResetToken);
		}
		
		String token = jwtUtils.generatePasswordResetToken(userEmail);
		PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity();
		
		passwordResetToken.setToken(token);
		passwordResetToken.setUser(user);
		
		passwordResetTokenRepository.save(passwordResetToken);
		
		emailService.sendPasswordResetEmail(userEmail, token);
		
	}

	@Override
	public void resetPassword(PasswordResetConfirmRequest passwordResetConfirm) {

		String token = passwordResetConfirm.getToken();
		
		try {
			jwtUtils.isTokenValid(token);
		} catch(ExpiredJwtException ex) {
			throw new CrimsonCodeGlobalException("Password Reset token has expired");
		} catch (Exception ex) {
			throw new CrimsonCodeGlobalException("Invliad Password Reset token");
		}
		
		PasswordResetTokenEntity passwordRsetTokenEntity =
				passwordResetTokenRepository.findByToken(token)
					.orElseThrow(() -> new CrimsonCodeGlobalException("Invliad Password Reset token"));
		
		String newPassword = passwordResetConfirm.getNewPassword();
		String confirmPassword = passwordResetConfirm.getConfirmNewPassword();
		
		if (!newPassword.equals(confirmPassword)) {
			throw new CrimsonCodeGlobalException("The new password and confirm password do not match");
		}
		
		UserEntity user = passwordRsetTokenEntity.getUser();
		
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setPasswordResetToken(null);
		userRepository.save(user);
		
		passwordResetTokenRepository.delete(passwordRsetTokenEntity);
		
	}
	
}
