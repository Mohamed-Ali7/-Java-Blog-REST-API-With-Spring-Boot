package com.crimson_code_blog_rest_apis.dto.response;

public class CommentResponseModel {

	private long id;
	private String userPublicId;
	private String content;
	private String createdAt;
	
	public CommentResponseModel() {
		
	}

	public CommentResponseModel(long id, String userPublicId, String content, String createdAt) {
		this.id = id;
		this.userPublicId = userPublicId;
		this.content = content;
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserPublicId() {
		return userPublicId;
	}

	public void setUserPublicId(String userPublicId) {
		this.userPublicId = userPublicId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
