package com.crimson_code_blog_rest_apis.service;

import java.util.List;

import com.crimson_code_blog_rest_apis.dto.request.PostRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;

public interface PostService {

	PostResponseModel createPost(PostRequestModel postRequest);
	
	List<PostResponseModel> getAllPosts(int page, int pageSize, String sortBy);
	
	PostResponseModel getPost(long id);
	
	PostResponseModel updatePost(long id, PostRequestModel postRequest);

	void deletePost(long id);
	
}
