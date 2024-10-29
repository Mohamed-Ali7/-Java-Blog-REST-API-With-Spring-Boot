package com.crimson_code_blog_rest_apis.dto.response;

public class PostResponseModel {

	private long id;
	private String title;
	private String content;
	private String userPublicId;
	private String createdAt;
	
	public PostResponseModel() {
		
	}

	public PostResponseModel(long id, String title, String content, String userPublicId, String createdAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.userPublicId = userPublicId;
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getUserPublicId() {
		return userPublicId;
	}

	public void setUserPublicId(String userPublicId) {
		this.userPublicId = userPublicId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
