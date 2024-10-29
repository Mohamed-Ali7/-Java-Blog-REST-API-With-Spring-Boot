package com.crimson_code_blog_rest_apis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.CommentRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.CommentResponseModel;
import com.crimson_code_blog_rest_apis.entity.CommentEntity;
import com.crimson_code_blog_rest_apis.entity.PostEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.CommentRepository;
import com.crimson_code_blog_rest_apis.repository.PostRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private PostRepository postRepository;
	private CommentRepository commentRepository;
	private UserRepository userRepository;
	private DateTimeFormatter dateTimeFormatter;
	private ModelMapper modelMapper;
	
	@Autowired
	public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository,
			UserRepository userRepository, DateTimeFormatter dateTimeFormatter, ModelMapper modelMapper) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.dateTimeFormatter = dateTimeFormatter;
		this.modelMapper = modelMapper;
	}

	@Override
	public CommentResponseModel createComment(long postId, CommentRequestModel commentRequest) {

		// Get the current authenticated user to assign it to the comment he/she is creating
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		UserEntity authenticatedUser = userRepository.findByEmail(auth.getName())
				.orElseThrow(() ->
						new ResourceNotFoundException("User does not exist with email: " + auth.getName()));

		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> 
						new ResourceNotFoundException("Post does not exist with id: " + postId));
		
		CommentEntity newComment = modelMapper.map(commentRequest, CommentEntity.class);
		
		newComment.setCreatedAt(LocalDateTime.now());
		newComment.setUser(authenticatedUser);
		newComment.setPost(post);
		newComment.setUserPublicId(authenticatedUser.getPublicId());

		commentRepository.save(newComment);
		
		CommentResponseModel commentResponse = mapToCommentResponse(newComment);
		
		return commentResponse;
	}

	@Override
	public List<CommentResponseModel> getAllCommentsByPost(long postId, int page, int pageSize, String sortBy) {

		if (page > 0) {
			page -= 1;
		}
		
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> 
						new ResourceNotFoundException("Post does not exist with id: " + postId));
		
		Sort sort = Sort.by(sortBy);
		
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		
		Page<CommentEntity> commentsPage = commentRepository.findAllByPostId(postId, pageable);
		
		List<CommentEntity> postComments = commentsPage.getContent();
		
		List<CommentResponseModel> commentsResponse = postComments.stream()
				.map(comment -> mapToCommentResponse(comment)).collect(Collectors.toList());
		
		return commentsResponse;
	}

	@Override
	public CommentResponseModel getComment(long postId, long commentId) {

		CommentEntity comment = commentRepository.findById(commentId)
				.orElseThrow(() -> 
					new ResourceNotFoundException("Comment does not exist with id: " + commentId));
		
		PostEntity commentPost = comment.getPost();
		
		if (commentPost == null) {
			throw new ResourceNotFoundException("Post does not exist with id: " + postId);
		}
		
		if(commentPost.getId() != postId) {
			throw new CrimsonCodeGlobalException("There is no such comment for this post");
		}
		
		CommentResponseModel commentResponse = mapToCommentResponse(comment);
		
		return commentResponse;
	}

	@Override
	public CommentResponseModel updateComment(long postId, long commentId, CommentRequestModel commentRequest) {
		
		// Get the current authenticated user to check if he/she is the owner of the comment
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		UserEntity authenticatedUser = userRepository.findByEmail(auth.getName())
				.orElseThrow(() ->
				new ResourceNotFoundException("User does not exist with email: " + auth.getName()));
		
		PostEntity post = postRepository.findById(postId).orElseThrow(() -> 
			new ResourceNotFoundException("Post does not exist with id: " + postId));

		CommentEntity comment = commentRepository.findById(commentId)
				.orElseThrow(() -> 
					new ResourceNotFoundException("Comment does not exist with id: " + commentId));
		
		if(comment.getPost().getId() != post.getId()) {
			throw new CrimsonCodeGlobalException("There is no such comment for this post");
		}

		if (!comment.getUserPublicId().equals(authenticatedUser.getPublicId())) {
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to update this comment", 
							authenticatedUser.getEmail()));
		}
		
		comment.setContent(commentRequest.getContent());
		
		commentRepository.save(comment);
		
		CommentResponseModel commentResponse = mapToCommentResponse(comment);

		return commentResponse;
	}

	@Override
	public void deleteComment(long postId, long commentId) {
		// Get the current authenticated user to check if he/she is the owner of the comment
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
		UserEntity authenticatedUser = userRepository.findByEmail(auth.getName())
				.orElseThrow(() ->
				new ResourceNotFoundException("User does not exist with email: " + auth.getName()));

		PostEntity post = postRepository.findById(postId).orElseThrow(() -> 
		new ResourceNotFoundException("Post does not exist with id: " + postId));

		CommentEntity comment = commentRepository.findById(commentId)
				.orElseThrow(() -> 
					new ResourceNotFoundException("Comment does not exist with id: " + commentId));
	
		if(comment.getPost().getId() != post.getId()) {
			throw new CrimsonCodeGlobalException("There is no such comment for this post");
		}

		if (!comment.getUserPublicId().equals(authenticatedUser.getPublicId())) {
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to delete this comment", 
							authenticatedUser.getEmail()));
		}
		
		commentRepository.delete(comment);
	}
	
	private CommentResponseModel mapToCommentResponse(CommentEntity commentEntity) {

		CommentResponseModel commentResponse = new CommentResponseModel();

		commentResponse.setId(commentEntity.getId());
		commentResponse.setUserPublicId(commentEntity.getUserPublicId());
		commentResponse.setContent(commentEntity.getContent());
		commentResponse.setCreatedAt(dateTimeFormatter.format(commentEntity.getCreatedAt()));

		return commentResponse;
	}

}
