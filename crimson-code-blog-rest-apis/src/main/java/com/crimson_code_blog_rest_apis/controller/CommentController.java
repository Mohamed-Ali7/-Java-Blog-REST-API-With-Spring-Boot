package com.crimson_code_blog_rest_apis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.CommentRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.CommentResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.service.CommentService;
import com.crimson_code_blog_rest_apis.utils.OperationName;
import com.crimson_code_blog_rest_apis.utils.OperationStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

	private CommentService commentService;
	
	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@PostMapping
	public ResponseEntity<CommentResponseModel> createComment(@PathVariable long postId,
												@RequestBody CommentRequestModel commentRequest) {
		
		return new ResponseEntity<>(commentService.createComment(postId, commentRequest), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<CommentResponseModel> getAllPostComments(@PathVariable long postId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize,
			@RequestParam(value = "sort", defaultValue = "id") String sortBy) {
		
		return commentService.getAllCommentsByPost(postId, page, pageSize, sortBy);
	}
	
	@GetMapping("/{commentId}")
	public CommentResponseModel getComment(@PathVariable long postId, @PathVariable long commentId) {
		return commentService.getComment(postId, commentId);
	}
	
	@PutMapping("/{commentId}")
	public CommentResponseModel updateComment(@PathVariable long postId, @PathVariable long commentId,
												@RequestBody CommentRequestModel commentRequest) {
		return commentService.updateComment(postId, commentId, commentRequest);
	}
	
	@DeleteMapping("/{commentId}")
	public OperationStatusResponse deleteComment(@PathVariable long postId, @PathVariable long commentId) {
		
		commentService.deleteComment(postId, commentId);
		
		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		operationStatus.setOperationName(OperationName.DELETE_COMMENT.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("The comment has been deleted successfully");
		
		return operationStatus;
	}
}
