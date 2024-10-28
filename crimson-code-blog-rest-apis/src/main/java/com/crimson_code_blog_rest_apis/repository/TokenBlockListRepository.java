package com.crimson_code_blog_rest_apis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimson_code_blog_rest_apis.entity.TokenBlockListEntity;

public interface TokenBlockListRepository extends JpaRepository<TokenBlockListEntity, Long> {
	Optional<TokenBlockListEntity> findByToken(String token);
	boolean existsByToken(String token);
}
