package com.crimson_code_blog_rest_apis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.EmailVerificationRequest;
import com.crimson_code_blog_rest_apis.dto.request.LoginRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.LogoutRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.LoginResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.RefreshTokenResponse;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.TokenBlockListEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exception.EmailVerificationException;
import com.crimson_code_blog_rest_apis.exception.RefreshTokenException;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.RoleRepository;
import com.crimson_code_blog_rest_apis.repository.TokenBlockListRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.service.AuthService;
import com.crimson_code_blog_rest_apis.service.EmailService;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;
import com.crimson_code_blog_rest_apis.utils.UserRoles;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

import com.crimson_code_blog_rest_apis.security.UserPrincipal;

@Service
public class AuthServiceImpl implements AuthService {
	
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private DateTimeFormatter dateFormatter;
	private EmailService emailService;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;
	private TokenBlockListRepository tokenBlockListRepository;
	
	@Autowired
	public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
					DateTimeFormatter dateFormatter, EmailService emailService,
					RoleRepository roleRepository, PasswordEncoder passwordEncoder,
					AuthenticationManager authenticationManager, JwtUtils jwtUtils,
					TokenBlockListRepository tokenBlockListRepository) {

		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.dateFormatter = dateFormatter;
		this.emailService = emailService;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.tokenBlockListRepository = tokenBlockListRepository; 
	}

	@Override
	public RegisterResponseModel register(RegisterRequestModel registerRequestModel) {
		
		if (userRepository.findByEmail(registerRequestModel.getEmail()).isPresent()) {
			throw new CrimsonCodeGlobalException("This email already exists");
		}
		
		UserEntity newUser = modelMapper.map(registerRequestModel, UserEntity.class);
		
		newUser.setPublicId(UUID.randomUUID().toString());
		newUser.setJoinedAt(LocalDateTime.now());
		
		RoleEntity userRole = roleRepository.findByName(UserRoles.ROLE_USER.name())
				.orElseGet(() -> {
					RoleEntity roleUser = new RoleEntity(UserRoles.ROLE_USER.name());
					return roleRepository.save(roleUser);
				});
		
		String emailVerificationToken = jwtUtils.generateEmailVerificationToken(newUser.getEmail());
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
	public LoginResponseModel login(LoginRequestModel loginRequest) {
		
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getEmail(), loginRequest.getPassword());
		
		Authentication authenticatedToken = authenticationManager.authenticate(authenticationToken);
		
		UserPrincipal userPrincipal = (UserPrincipal) authenticatedToken.getPrincipal();
		
		SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
		
		LoginResponseModel loginResponse = new LoginResponseModel();
		
		List<String> userRoles = authenticatedToken.getAuthorities().stream()
				.map(authority -> authority.getAuthority()).collect(Collectors.toList());
		Map<String, Object> userClaims = new HashMap<>();
		
		userClaims.put("roles", userRoles);
		userClaims.put("userPublicId", userPrincipal.getPublicId());
		
		loginResponse.setAccessToken(jwtUtils.generateAccessToken(
				loginRequest.getEmail(), userClaims));
		loginResponse.setRefreshToken(jwtUtils.generateRefreshToken(loginRequest.getEmail()));
		
		return loginResponse;
	}
	
	@Override
	public void emailVerification(String verificationToken) {
		
		try {
			jwtUtils.isTokenValid(verificationToken);
		} catch(ExpiredJwtException ex) {
			throw new EmailVerificationException("Email verification token has expired");
		} catch (Exception ex) {
			throw new EmailVerificationException("Invliad email verification token");
		}

		UserEntity user = userRepository.findByEmailVerificationToken(verificationToken)
				.orElseThrow(() -> new EmailVerificationException("Invalid email verification token"));
		
		user.setUserIsVerified(true);
		user.setEmailVerificationToken(null);
		
		userRepository.save(user);
	}

	@Override
	public void emailVerificationRequest(EmailVerificationRequest verificationRequest) {

		String userEmail = verificationRequest.getEmail();
		UserEntity user = userRepository.findByEmail(verificationRequest.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException(
						"User does not exist with email: " + userEmail));
		
		if (user.getUserIsVerified()) {
			throw new EmailVerificationException("Your email has been already verified");
		}

		String emailVerificationToken = jwtUtils.generateEmailVerificationToken(userEmail);
		emailService.sendVerificationEmail(userEmail, emailVerificationToken);

		// Sends a new email verification token to user's email address
		user.setEmailVerificationToken(emailVerificationToken);
		
		userRepository.save(user);

	}

	@Override
	public RefreshTokenResponse refreshAccessToken(HttpServletRequest request) {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (header == null || !header.startsWith("Bearer ")) {
			throw new RefreshTokenException("Invalid Refresh token");
		}
		
		String refreshToken = header.substring(7);
		
		try {
			jwtUtils.isTokenValid(refreshToken);
		} catch(ExpiredJwtException ex) {
			throw new RefreshTokenException("Refresh token has expired");
		} catch (Exception ex) {
			throw new RefreshTokenException("Invliad Refresh token");
		}
		
		String userEmail = jwtUtils.extractUsername(refreshToken);
		
		UserEntity user = userRepository.findByEmail(userEmail)
				.orElseThrow(() ->
					new ResourceNotFoundException("User does not exist with email: " + userEmail));
		
		Map<String, Object> userClaims = new HashMap<>();

		List<String> userRoles = user.getRoles().stream().map(role -> role.getName()).toList();
		
		userClaims.put("roles", userRoles);
		userClaims.put("userPublicId", user.getPublicId());
		
		String newAccessToken = jwtUtils.generateAccessToken(userEmail, userClaims);
		
		RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
		refreshTokenResponse.setToken(newAccessToken);

		return refreshTokenResponse;
	}

	@Override
	public void logout(LogoutRequestModel logoutRequest, HttpServletRequest request) {
		
		String refreshToken = logoutRequest.getRefreshToken();
		try {
			jwtUtils.isTokenValid(refreshToken);
		} catch (Exception e) {
			throw new CrimsonCodeGlobalException("Invalid refresh token");
		}
		
		String AuthorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String accessToken = AuthorizationHeader.substring(7);

		TokenBlockListEntity blockListForRefreshToken = new TokenBlockListEntity(refreshToken);
		TokenBlockListEntity blockListForAccessToken = new TokenBlockListEntity(accessToken);
		
		tokenBlockListRepository.saveAll(new ArrayList<>(
											List.of(blockListForRefreshToken, blockListForAccessToken)));
		
	}

}
