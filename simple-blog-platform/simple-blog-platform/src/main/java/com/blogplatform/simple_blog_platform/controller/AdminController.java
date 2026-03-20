package com.blogplatform.simple_blog_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blogplatform.simple_blog_platform.model.Post;
import com.blogplatform.simple_blog_platform.service.PostService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final PostService postService;

	public AdminController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/posts")
	public String showPostListDashboard(Model model)
	{
		List<Post> allPosts = postService.findAllPosts();
		model.addAttribute("posts", allPosts);
		return "admin/list-posts";
	}
	
	@GetMapping("/posts/new")
	public String showNewPostForm(Model model) {
		Post post = new Post();
		 model.addAttribute("post", post);
		 return "admin/post-form";
	}
	@PostMapping("/posts")
    public String savePost(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult, Principal principal) {
		  if (bindingResult.hasErrors())
		  {
			  return "admin/post-form";
		  }
        String username = principal.getName();
        postService.savePost(post, username);
        return "redirect:/admin/posts";
	}
	
	 @GetMapping("/posts/edit/{id}")
	 public String showEditPostForm(@PathVariable Long id, Model model) {
		 Post post = postService.findPostById(id)
                 .orElseThrow(() -> new IllegalArgumentException("Invalid post ID:" + id));
	        model.addAttribute("post", post);
	        return "admin/post-form";
	 }
	 @GetMapping("/posts/delete/{id}")
	    public String deletePost(@PathVariable Long id) {
		 postService.deletePostById(id);
		 return "redirect:/admin/posts";
	 }
}
