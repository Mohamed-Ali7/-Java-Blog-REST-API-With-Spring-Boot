package com.crimson_code_blog_rest_apis.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "public_id", nullable = false, unique = true, length = 60)
	private String publicId;
	
	@Column(name = "email", nullable = false, unique = true, length = 120)
	private String email;
	
	@Column(name = "password", nullable = false, length = 100)
	private String password;
	
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;
	
	@Column(name = "joined_at", columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private LocalDateTime joinedAt;
	
	@Column(name = "email_verification_token", nullable = true, length = 250)
	private String emailVerificationToken;

	@Column(name = "is_user_verified", nullable = false)
	private boolean userIsVerified = false;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {
			CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.REFRESH
	})
	@JoinTable(name = "users_roles",
	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private List<RoleEntity> roles;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PostEntity> posts;
	
	public UserEntity() {
		
	}

	public UserEntity(String publicId, String email, String password, String firstName, String lastName) {
		this.publicId = publicId;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(LocalDateTime joinedAt) {
		this.joinedAt = joinedAt;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
	
	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public boolean getUserIsVerified() {
		return userIsVerified;
	}

	public void setUserIsVerified(boolean isUserVerified) {
		this.userIsVerified = isUserVerified;
	}

	public List<PostEntity> getPosts() {
		return posts;
	}

	public void setPosts(List<PostEntity> posts) {
		this.posts = posts;
	}

	public void addRole(RoleEntity role) {
		if (this.roles == null) {
			this.roles = new ArrayList<>();
		}
		
		this.roles.add(role);
	}
}
