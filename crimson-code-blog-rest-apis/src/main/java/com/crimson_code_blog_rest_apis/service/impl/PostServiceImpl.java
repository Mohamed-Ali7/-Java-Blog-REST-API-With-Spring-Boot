package com.crimson_code_blog_rest_apis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.PostRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
import com.crimson_code_blog_rest_apis.entity.PostEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exception.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.CommentRepository;
import com.crimson_code_blog_rest_apis.repository.PostRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.service.PostService;
import com.crimson_code_blog_rest_apis.utils.UserRoles;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;

@Service
public class PostServiceImpl implements PostService {
	
	private PostRepository postRepository;
	private UserRepository userRepository;
	private DateTimeFormatter dateTimeFormatter;
	private ModelMapper modelMapper;
	
	@Autowired
	public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
			DateTimeFormatter dateTimeFormatter, ModelMapper modelMapper) {

		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.dateTimeFormatter = dateTimeFormatter;
		this.modelMapper = modelMapper;
	}

	@Override
	public PostResponseModel createPost(PostRequestModel postRequest) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		UserEntity user = userRepository.findByEmail(auth.getName())
				.orElseThrow(() -> 
						new ResourceNotFoundException("User does not exist with email: " + auth.getName()));
		
		PostEntity newPost = modelMapper.map(postRequest, PostEntity.class);
		
		newPost.setUserPublicId(user.getPublicId());
		newPost.setUser(user);
		newPost.setCreatedAt(LocalDateTime.now());
		
		postRepository.save(newPost);
		
		PostResponseModel postResponse = mapToPostResponse(newPost);
		
		return postResponse;
	}

	@Override
	public List<PostResponseModel> getAllPosts(int page, int pageSize, String sortBy) {
		
		if(page > 0) {
			page -= 1;
		}
		
		Sort sort = Sort.by(sortBy);
		
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		
		Page <PostEntity> postEntityPages = postRepository.findAll(pageable);
	
		List<PostEntity> postEntity = postEntityPages.getContent();
		
		
		List<PostResponseModel> postsResponse = postEntity.stream()
		.map(post -> mapToPostResponse(post)).collect(Collectors.toList());

		return postsResponse;
	}

	@Override
	public PostResponseModel getPost(long id) {
		PostEntity post = postRepository.findById(id)
				.orElseThrow(()->
					new ResourceNotFoundException("Post does not exist with id: " + id));
		
		PostResponseModel postResponse = mapToPostResponse(post);
		
		return postResponse;
	}

	@Override
	public PostResponseModel updatePost(long id, PostRequestModel postRequest) {
		//Get current authenticated user to check if he has ADMIN role or he is the one who created this post
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
				
		UserPrincipal userPrincipal = (UserPrincipal) authenticatedUser.getPrincipal();

						
		PostEntity postEntity = postRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Post does not exist with id: " + id));
				
		if(!(userPrincipal.getPublicId().equals(postEntity.getUserPublicId())) && 
				!userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
					
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to update this post", 
							authenticatedUser.getName()));
		}
				
		postEntity.setContent(postRequest.getContent());
		postEntity.setTitle(postRequest.getTitle());
				
		PostEntity updatedPost = postRepository.save(postEntity);
		
		PostResponseModel postResponse = mapToPostResponse(updatedPost);
		
		return postResponse;
	}

	@Override
	public void deletePost(long id) {
		//Get current authenticated user to check if he has ADMIN role or he is the one who created this post
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
					
		UserPrincipal userPrincipal = (UserPrincipal) authenticatedUser.getPrincipal();

								
		PostEntity post = postRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Post does not exist with id: " + id));
						
		if(!(userPrincipal.getPublicId().equals(post.getUserPublicId())) && 
				!userPrincipal.getAuthorities()
						.contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name()))) {
							
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to delete this post", 
							authenticatedUser.getName()));
		}
		
		postRepository.delete(post);

	}
	
	private PostResponseModel mapToPostResponse(PostEntity postEntity) {
		PostResponseModel postResponse = new PostResponseModel();
		
		postResponse.setId(postEntity.getId());
		postResponse.setTitle(postEntity.getTitle());
		postResponse.setContent(postEntity.getContent());
		postResponse.setUserPublicId(postEntity.getUserPublicId());
		postResponse.setCreatedAt(dateTimeFormatter.format(postEntity.getCreatedAt()));
				
		return postResponse;
	}

}
