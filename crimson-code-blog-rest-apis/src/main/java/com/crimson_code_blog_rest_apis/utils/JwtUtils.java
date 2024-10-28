package com.crimson_code_blog_rest_apis.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.repository.TokenBlockListRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

	@Value("${tokenSecert}")
	private String secretKey;
	
	@Value("${accessTokenExpirationDate}")
	private long accessTokenExpirationDate;
	
	@Value("${refreshTokenExpirationDate}")
	private long refreshTokenExpirationDate;
	
	@Value("${emailVerificationTokenExpirationDate}")
	private long emailVerificationTokenExpirationDate;
	
	@Value("${passwordResetTokenExpirationDate}")
	private long passwordResetTokenExpirationDate;
	
	private TokenBlockListRepository tokenBlockListRepository;
	
	public JwtUtils() {

	}
	
	@Autowired
	public JwtUtils(TokenBlockListRepository tokenBlockListRepository) {
		this.tokenBlockListRepository = tokenBlockListRepository;
	}
	
	public String generateAccessToken(String username, Map<String, Object> claims) {
		return generateJwtToken(username, claims, accessTokenExpirationDate);
	}
	
	public String generateRefreshToken(String username) {
		return generateJwtToken(username, new HashMap<>(), refreshTokenExpirationDate);
	}
	
	public String generateEmailVerificationToken(String username) {
		return generateJwtToken(username, new HashMap<>(), emailVerificationTokenExpirationDate);
	}
	
	public String generatePasswordResetToken(String username) {
		return generateJwtToken(username, new HashMap<>(), passwordResetTokenExpirationDate);
	}
	
	private String generateJwtToken(String username, Map<String, Object> claims, long expirationDate) {
		return Jwts.builder()
				.subject(username)
				.claims(claims)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationDate))
				.signWith(key())
				.compact();
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(key())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public <T> T getClaim(String token, Function <Claims, T> claimResolver) {
		Claims claims = extractAllClaims(token);

		return claimResolver.apply(claims);
	}
	
	public String extractUsername(String token) {
		return getClaim(token, claims -> claims.getSubject());
	}
	
	private SecretKey key() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public boolean isTokenValid(String token) {

		Jwts.parser()
				.verifyWith(key())
				.build()
				.parse(token);

		return true;
	}
	
	public boolean isTokenBlocked(String token) {
		return tokenBlockListRepository.existsByToken(token);
	}
}
