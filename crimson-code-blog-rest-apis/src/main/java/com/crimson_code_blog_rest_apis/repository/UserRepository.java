package com.crimson_code_blog_rest_apis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimson_code_blog_rest_apis.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String email);
	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByEmailVerificationToken (String emailVerificationToken);
}
