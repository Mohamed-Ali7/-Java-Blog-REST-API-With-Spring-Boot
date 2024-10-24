package com.crimson_code_blog_rest_apis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(customizer -> customizer.disable())
		.authorizeHttpRequests(authorize -> 
			authorize.requestMatchers("/api/auth/**").permitAll()
			.requestMatchers("error").permitAll()
			.anyRequest().authenticated()
		)
		.httpBasic(Customizer.withDefaults())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
}
