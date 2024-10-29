package com.crimson_code_blog_rest_apis.service;

import java.util.List;

import com.crimson_code_blog_rest_apis.dto.request.CommentRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.CommentResponseModel;

public interface CommentService {

	CommentResponseModel createComment(long postId, CommentRequestModel commentDto);

	List<CommentResponseModel> getAllCommentsByPost(long postId, int page, int pageSize, String sortBy);
	
	CommentResponseModel getComment(long postId, long commentId);
	
	CommentResponseModel updateComment(long postId, long commentId, CommentRequestModel commentDto);
	
	void deleteComment(long postId, long commentId);
}
