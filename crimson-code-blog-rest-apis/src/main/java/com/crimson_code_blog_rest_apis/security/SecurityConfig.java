package com.crimson_code_blog_rest_apis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.crimson_code_blog_rest_apis.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private UserDetailsService userDetailsService;
	private JwtUtils jwtUtils;
	private ApplicationContext applicationContext;
	private HandlerExceptionResolver exceptionResolver;
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, JwtUtils jwtUtils,
			ApplicationContext applicationContext,
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
		this.userDetailsService = userDetailsService;
		this.jwtUtils = jwtUtils;
		this.applicationContext = applicationContext;
		this.exceptionResolver = exceptionResolver;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorize -> 
			authorize.requestMatchers("/api/auth/logout").authenticated()
			.requestMatchers("/api/auth/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
			.requestMatchers("/api/users/password-reset-request").permitAll()
			.requestMatchers("/api/users/password-reset").permitAll()
			.requestMatchers("error").permitAll()
			.anyRequest().authenticated()
		)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(new JwtAuthenticationFilter(jwtUtils, applicationContext, exceptionResolver),
				UsernamePasswordAuthenticationFilter.class)
		
		// This handles the unauthorized exceptions manually to return 401
		// because spring by default return 403 when the user is unauthorized
		// instead of 401 when using custom login functionality
		// like JWT in our case
		.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, exception) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
		}))
		.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncode());
		
		return daoAuthenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
