package com.crimson_code_blog_rest_apis.service.impl;

import org.springframework.data.domain.Pageable;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.ChangePasswordRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.entity.PasswordResetTokenEntity;
import com.crimson_code_blog_rest_apis.entity.PostEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.PasswordResetTokenRepository;
import com.crimson_code_blog_rest_apis.repository.PostRepository;
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
	private PostRepository postRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
			DateTimeFormatter dateFormatter, EmailService emailService, JwtUtils jwtUtils,
			PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder,
			PostRepository postRepository) {

		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.dateFormatter = dateFormatter;
		this.emailService = emailService;
		this.jwtUtils = jwtUtils;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.postRepository = postRepository;
	}
	
	@Override
	public List<UserResponseModel> getAllUsers(int page, int pageSize, String sortBy) {

		Sort sort = Sort.by(sortBy);
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		
		Page<UserEntity> usersPage = userRepository.findAll(pageable);
		
		List<UserEntity> users = usersPage.getContent();
		
		Type typeList = new TypeToken<List<UserResponseModel>>() {}.getType();
		
		List<UserResponseModel> usersResponse = modelMapper.map(users, typeList);
		return usersResponse;
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
	public UserResponseModel updateUser(String publicId, UpdateUserRequestModel updateRequest) {

		UserEntity user = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		user.setFirstName(updateRequest.getFirstName());
		user.setLastName(updateRequest.getLastName());
		
		userRepository.save(user);
		
		UserResponseModel userResponse = modelMapper.map(user, UserResponseModel.class);

		return userResponse;
	}
	
	@Override
	public void deleteUser(String publicId) {

		UserEntity user = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		user.getRoles().forEach(role -> {
			if(role.getName().equals("ROLE_ADMIN")) {
				throw new CrimsonCodeGlobalException("Users with ADMIN role can't be deleted");
			}
		});

		userRepository.delete(user);
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
		userRepository.save(user);
		
		passwordResetTokenRepository.delete(passwordRsetTokenEntity);
		
	}

	@Override
	public void changePassword(String publicId, ChangePasswordRequestModel changePasswordRequest) {
		
		UserEntity user = userRepository.findByPublicId(publicId).orElseThrow(() -> 
				new ResourceNotFoundException("User does not exist with id: " + publicId));
	
		String currentPassword = changePasswordRequest.getCurrentPassword();
		String newPassword = changePasswordRequest.getNewPassword();
		String confirmPassword = changePasswordRequest.getConfirmPassword();
		
		if (!newPassword.equals(confirmPassword)) {
			throw new CrimsonCodeGlobalException("The new password and confirm password do not match");
		}
		
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new CrimsonCodeGlobalException("The current password you've entered is incorrect");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

	}

	@Override
	public List<PostResponseModel> getUserPosts(String publicId, int page, int pageSize, String sortBy) {

		UserEntity user = userRepository.findByPublicId(publicId)
				.orElseThrow(() ->
						new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		Sort sort = Sort.by(sortBy);
		
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		
		Page<PostEntity> userPostsPage = postRepository.findByUserId(user.getId(), pageable);
		
		List<PostEntity> userPosts = userPostsPage.getContent();
		
		List<PostResponseModel> userPostsResponse = userPosts.stream()
				.map(post -> {
					PostResponseModel postResponse = new PostResponseModel();
					
					postResponse.setId(post.getId());
					postResponse.setTitle(post.getTitle());
					postResponse.setContent(post.getContent());
					postResponse.setUserPublicId(post.getUserPublicId());
					postResponse.setCreatedAt(dateFormatter.format(post.getCreatedAt()));
							
					return postResponse;
				}).collect(Collectors.toList());
		
		return userPostsResponse;
	}

}
