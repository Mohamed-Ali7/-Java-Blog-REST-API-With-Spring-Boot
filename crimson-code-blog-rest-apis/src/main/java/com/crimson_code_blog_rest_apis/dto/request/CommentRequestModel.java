package com.crimson_code_blog_rest_apis.dto.request;

public class CommentRequestModel {

	private String content;
	
	public CommentRequestModel() {
		
	}

	public CommentRequestModel(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
