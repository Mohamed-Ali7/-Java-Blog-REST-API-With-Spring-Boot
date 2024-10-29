package com.crimson_code_blog_rest_apis.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class PostEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "title", nullable = false, length = 255)
	private String title;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "user_public_id", nullable = false)
	private String userPublicId;
	
	@Column(name = "created_at", columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CommentEntity> comments;

	public PostEntity() {
		
	}
	
	public PostEntity(String title, String content, String userPublicId, LocalDateTime createdAt) {
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
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public List<CommentEntity> getComments() {
		return comments;
	}

	public void setComments(List<CommentEntity> comments) {
		this.comments = comments;
	}
	
}
