package com.crimson_code_blog_rest_apis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	
	private final String FROM = "crimson_code@gmail.com";
	
	private final String EMAIL_VERIFICATION_SUBJECT =
			"Verify Your Email Address to Finish Your Registration Proccess";
	
	private final String PASSWORD_RESET_SUBJECT = "Password Reset Request";
	
	private final String EMAIL_VERIFICATION_HTML_BODY = "<h1>Please verify your email address</h1> "
			+ "<p>Click on the following link to verify your email address</p>"
			+ "<a href='http://localhost:8080/api/auth/email-verification?token=${tokenValue}'>"
			+ "Your link to complete your registration</a>";

	private final String EMAIL_VERIFICATION_TEXT_BODY = "Please verify your email address "
			+ "Go to the following link to verify your email address "
			+ "http://localhost:8080/api/auth/email-verification?token=${tokenValue} "
			+ "Final Step to complete your registration";
	
	private final String PASSWORD_RESET_HTML_BODY = "<h1>Due to Your Request to Reset Your Password</h1> "
			+ "<p>Click on the following link to proceed your password reset process</p>"
			+ "<a href='http://localhost:8080/api/auth/password/reset?token=${tokenValue}'>"
			+ "Your link to reset your password</a>";

	private final String PASSWORD_RESET_TEXT_BODY = "Due to Your Request to Reset Your Password "
			+ "Go to the following link to proceed your password reset process "
			+ "http://localhost:8080/api/auth/password/reset?token=${tokenValue} "
			+ "Final Step to complete your registration";
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendVerificationEmail(String username, String token) {
		String textBodyWithToken = EMAIL_VERIFICATION_TEXT_BODY.replace("${tokenValue}", token);
		String htmlBodyWithToken = EMAIL_VERIFICATION_HTML_BODY.replace("${tokenValue}", token);
		
		sendEmail(username, EMAIL_VERIFICATION_SUBJECT, textBodyWithToken, htmlBodyWithToken);
	}

	@Override
	public void sendPasswordResetEmail(String username, String token) {
		String textBodyWithToken = PASSWORD_RESET_TEXT_BODY.replace("${tokenValue}", token);
		String htmlBodyWithToken = PASSWORD_RESET_HTML_BODY.replace("${tokenValue}", token);
		
		sendEmail(username, PASSWORD_RESET_SUBJECT, textBodyWithToken, htmlBodyWithToken);
	}
	
	private void sendEmail(String sendTo, String subject, String textBody, String htmlBody) {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		
		try {
			mimeMessageHelper.setTo(sendTo);
			mimeMessageHelper.setFrom(FROM);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(htmlBody, true);
			mimeMessageHelper.setText(textBody);
			
		} catch (MessagingException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		javaMailSender.send(mimeMessage);
	}

}
