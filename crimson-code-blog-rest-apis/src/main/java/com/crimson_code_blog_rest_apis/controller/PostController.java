package com.crimson_code_blog_rest_apis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.PostRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
import com.crimson_code_blog_rest_apis.service.PostService;
import com.crimson_code_blog_rest_apis.utils.OperationName;
import com.crimson_code_blog_rest_apis.utils.OperationStatus;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private PostService postService;
	
	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping
	public ResponseEntity<PostResponseModel> createPost(@RequestBody PostRequestModel postRequest) {
		return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<PostResponseModel> getAllPosts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize,
			@RequestParam(value = "sort", defaultValue = "id") String sortBy) {
		
		return postService.getAllPosts(page, pageSize, sortBy);
	}
	
	@GetMapping("/{id}")
	public PostResponseModel getPost(@PathVariable long id) {
		return postService.getPost(id);
	}
	
	@PutMapping("/{id}")
	public PostResponseModel updatePost(@PathVariable long id, @RequestBody PostRequestModel postRequest) {
		return postService.updatePost(id, postRequest);
	}
	
	@DeleteMapping("/{id}")
	public OperationStatusResponse deletePost(@PathVariable long id) {

		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		postService.deletePost(id);
		
		operationStatus.setOperationName(OperationName.DELETE_POST.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("The post has been deleted successfully");
		
		return operationStatus;
	}
	
}
