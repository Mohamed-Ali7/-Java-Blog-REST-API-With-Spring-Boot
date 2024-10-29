package com.crimson_code_blog_rest_apis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CrimsonCodeBlogRestApisApplication(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {

		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(CrimsonCodeBlogRestApisApplication.class, args);
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {

		RoleEntity adminRole = roleRepository.findByName(UserRoles.ROLE_ADMIN.name())
				.orElseGet(() -> {
					RoleEntity roleAdmin = new RoleEntity(UserRoles.ROLE_ADMIN.name());
					return roleRepository.save(roleAdmin);
				});
		
		RoleEntity userRole = roleRepository.findByName(UserRoles.ROLE_USER.name())
				.orElseGet(() -> {
					RoleEntity roleUser = new RoleEntity(UserRoles.ROLE_USER.name());
					return roleRepository.save(roleUser);
				});
		
		UserEntity adminUser = userRepository.findByEmail("admin@blog.com")
				.orElseGet(() -> {
					UserEntity admin = new UserEntity();
					
					admin.setPublicId(UUID.randomUUID().toString());
					admin.setEmail("admin@blog.com");
					admin.setPassword(passwordEncoder.encode("admin"));
					admin.setFirstName("admin");
					admin.setLastName("admin");
					
					admin.setJoinedAt(LocalDateTime.now());
					admin.setUserIsVerified(true);
					return admin;
				});
		if (adminUser.getRoles() != null) {
			adminUser.getRoles().clear();
		}
		
		adminUser.addRole(adminRole);
		adminUser.addRole(userRole);
		
		userRepository.save(adminUser);
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
