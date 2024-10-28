package com.crimson_code_blog_rest_apis.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CrimsonCodeExceptionHandler {

	@ExceptionHandler(CrimsonCodeGlobalException.class)
	public ResponseEntity<ErrorResponse> handleCrimsonCodeGlobalException(CrimsonCodeGlobalException ex){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
														ex.getMessage(), new Date());
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
														ex.getMessage(), new Date());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmailVerificationException.class)
	public ResponseEntity<ErrorResponse> handleEmailVerificationException(EmailVerificationException ex){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
														ex.getMessage(), new Date());
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex){
		
		String message = "Account disabled due to unverified email address, please verifiy your email first";
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, new Date());
		
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(RefreshTokenException.class)
	public ResponseEntity<ErrorResponse> handleRefreshTokenException(RefreshTokenException ex){
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage(), new Date());

		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(AccessTokenException.class)
	public ResponseEntity<ErrorResponse> handleAccessTokenException(AccessTokenException ex){
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage(), new Date());

		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
}
