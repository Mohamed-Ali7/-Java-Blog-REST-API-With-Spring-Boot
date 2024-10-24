package com.crimson_code_blog_rest_apis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.crimson_code_blog_rest_apis.repository.RoleRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.utils.UserRoles;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class CrimsonCodeBlogRestApisApplication implements CommandLineRunner {

	private RoleRepository roleRepository;
	private UserRepository userRepository;

	@Autowired
	public CrimsonCodeBlogRestApisApplication(RoleRepository roleRepository, UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;

	}

	public static void main(String[] args) {
		SpringApplication.run(CrimsonCodeBlogRestApisApplication.class, args);
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {

		RoleEntity adminRole = roleRepository.findByName(UserRoles.ROLE_ADMIN.name())
				.orElseGet(() -> {
					RoleEntity admin = new RoleEntity(UserRoles.ROLE_ADMIN.name());
					return roleRepository.save(admin);
				});
		
		boolean isAdminExists = userRepository.existsByEmail("admin@blog.com");

		if (!isAdminExists) {
			UserEntity adminUser = new UserEntity();
			
			adminUser.setPublicId(UUID.randomUUID().toString());
			adminUser.setEmail("admin@blog.com");
			adminUser.setPassword("admin");
			adminUser.setFirstName("admin");
			adminUser.setLastName("admin");
			adminUser.addRole(adminRole);
			adminUser.setJoinedAt(LocalDateTime.now());
			adminUser.setIsUserVerified(true);

			userRepository.save(adminUser);
		}
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	DateTimeFormatter dateFormatter() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}
}
