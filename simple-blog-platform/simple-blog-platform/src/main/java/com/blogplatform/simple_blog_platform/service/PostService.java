package com.blogplatform.simple_blog_platform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.blogplatform.simple_blog_platform.exception.ResourceNotFoundException;
import com.blogplatform.simple_blog_platform.model.Post;
import com.blogplatform.simple_blog_platform.model.User;
import com.blogplatform.simple_blog_platform.repository.PostRepository;
import com.blogplatform.simple_blog_platform.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;

@Service
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	
	
	
	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	public Page<Post> findAllPosts(Pageable pageable)
	{
		return postRepository.findAll(pageable);
	}
	
	public Optional<Post> findPostById(Long id) {
	    return postRepository.findById(id);
	}
	
	@Transactional
	public Post savePost(Post postFromForm, String username) {
	    if (postFromForm.getId() == null) {
	        User author = userRepository.findByUsername(username)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
	        postFromForm.setUser(author);
	        postFromForm.setCreatedAt(LocalDateTime.now());
	        return postRepository.save(postFromForm);
	    } else {
	        Post existingPost = postRepository.findById(postFromForm.getId())
	                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postFromForm.getId()));
	        existingPost.setTitle(postFromForm.getTitle());
	        existingPost.setContent(postFromForm.getContent());
	        return postRepository.save(existingPost);
	    }
	}
	public void deletePostById(Long id) {
		postRepository.deleteById(id);
	}
	public Page<Post> searchByTitle(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }
	
}
