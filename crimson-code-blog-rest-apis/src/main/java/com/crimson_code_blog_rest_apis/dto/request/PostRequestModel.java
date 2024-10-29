package com.crimson_code_blog_rest_apis.dto.request;

public class PostRequestModel {

	private String title;
	private String content;
	
	public PostRequestModel() {
		
	}

	public PostRequestModel(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
