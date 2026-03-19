package com.blogplatform.simple_blog_platform.controller;

import org.springframework.ui.Model;
import java.util.List;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.blogplatform.simple_blog_platform.dto.CommentDto;
import com.blogplatform.simple_blog_platform.model.Post;
import com.blogplatform.simple_blog_platform.service.PostService;
import com.blogplatform.simple_blog_platform.service.CommentService;

@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Post> allPosts = postService.findAllPosts();
        model.addAttribute("posts", allPosts);
        return "home";
    }

    @GetMapping("/posts/{id}")
    public String showPostDetailPage(@PathVariable Long id, Model model) {
        Post post = postService.findPostById(id);
        if (post == null) {
            throw new IllegalArgumentException("Invalid post ID:" + id);
        }
        model.addAttribute("post", post);
        model.addAttribute("newComment", new CommentDto());
        return "post-detail";
    }

    @PostMapping("/posts/{postId}/comments")
    public String submitComment(@PathVariable Long postId,
                               @ModelAttribute("newComment") CommentDto commentDto,
                               Principal principal) {

        String username = principal.getName();

        commentService.saveComment(postId, username, commentDto);

        return "redirect:/posts/" + postId;
    }
}