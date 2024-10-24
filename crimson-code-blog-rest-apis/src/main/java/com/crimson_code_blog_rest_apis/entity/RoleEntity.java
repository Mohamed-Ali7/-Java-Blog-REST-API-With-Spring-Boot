package com.crimson_code_blog_rest_apis.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.REFRESH
	})
	List<UserEntity> users;
	
	public RoleEntity() {
		
	}

	public RoleEntity(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	
}
