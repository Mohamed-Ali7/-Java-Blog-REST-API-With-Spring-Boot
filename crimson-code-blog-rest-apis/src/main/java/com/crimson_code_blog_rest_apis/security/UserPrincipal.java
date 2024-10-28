package com.crimson_code_blog_rest_apis.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.crimson_code_blog_rest_apis.entity.UserEntity;

public class UserPrincipal implements UserDetails {

	private UserEntity userEntity;
	
	private String publicId;

	public UserPrincipal() {
		
	}
	
	public UserPrincipal(UserEntity userEntity) {
		this.userEntity = userEntity;
		this.publicId = userEntity.getPublicId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = userEntity.getRoles().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName()))
				.collect(Collectors.toList());

		return authorities;
	}

	@Override
	public String getPassword() {
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getEmail();
	}
	
	@Override
	public boolean isEnabled() {
		return userEntity.getUserIsVerified();
	}

	public String getPublicId() {
		return this.publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
}
