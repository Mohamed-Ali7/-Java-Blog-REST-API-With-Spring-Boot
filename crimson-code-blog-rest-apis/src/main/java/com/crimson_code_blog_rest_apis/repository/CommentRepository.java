package com.crimson_code_blog_rest_apis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crimson_code_blog_rest_apis.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	Page<CommentEntity> findAllByPostId(long postId, Pageable pageable);
}
