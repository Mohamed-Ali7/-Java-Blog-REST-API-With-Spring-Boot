package com.crimson_code_blog_rest_apis.security;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.crimson_code_blog_rest_apis.exception.AccessTokenException;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;
	private ApplicationContext applicationContext;
	private HandlerExceptionResolver exceptionResolver;
	

	public JwtAuthenticationFilter(JwtUtils jwtUtils, ApplicationContext applicationContext,
			HandlerExceptionResolver exceptionResolver) {
		this.jwtUtils = jwtUtils;
		this.applicationContext = applicationContext;
		this.exceptionResolver = exceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = null;
		String username = null;
		
		if(request.getServletPath().equals("/api/auth/refresh")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);

			try {
				try {
					jwtUtils.isTokenValid(token);
				} catch (ExpiredJwtException ex) {
					throw new AccessTokenException("Access Token has expired");
				} catch (Exception ex) {
					throw new AccessTokenException("Invalid Access token - " + ex.getMessage());
				}
			} catch (Exception ex) {
				// Redirect the exception to be handled by the Custom Exception handler
				// which in our case CrimsonCodeExceptionHandler.class
				exceptionResolver.resolveException(request, response, null, ex);
				
				// return to prevent the code from going beyond this point
				return;
			}

			username = jwtUtils.extractUsername(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
																&& !jwtUtils.isTokenBlocked(token)) {
			
			UserDetails user = applicationContext
					.getBean(UserDetailsService.class).loadUserByUsername(username);
			
			Authentication authToken =
					new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		
		filterChain.doFilter(request, response);

	}

}
