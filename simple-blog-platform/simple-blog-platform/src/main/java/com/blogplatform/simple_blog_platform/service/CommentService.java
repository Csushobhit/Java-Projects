package com.blogplatform.simple_blog_platform.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogplatform.simple_blog_platform.dto.CommentDto;
import com.blogplatform.simple_blog_platform.exception.ResourceNotFoundException;
import com.blogplatform.simple_blog_platform.model.Comment;
import com.blogplatform.simple_blog_platform.model.Post;
import com.blogplatform.simple_blog_platform.model.User;
import com.blogplatform.simple_blog_platform.repository.CommentRepository;
import com.blogplatform.simple_blog_platform.repository.PostRepository;
import com.blogplatform.simple_blog_platform.repository.UserRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveComment(Long postId, String username, CommentDto commentDto) {

        Post post = postRepository.findById(postId)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        User user = userRepository.findByUsername(username)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(user);

        commentRepository.save(comment);
    }
}