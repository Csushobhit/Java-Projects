package com.blogplatform.simple_blog_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blogplatform.simple_blog_platform.model.User;
import com.blogplatform.simple_blog_platform.service.UserService;

@Controller
public class AuthController {
	private final UserService userService;
	public AuthController(UserService userService) {
        this.userService = userService;
    }
	@GetMapping("/register")
	public String showRegistartionForm(Model model)
	{
		model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("registrationSuccess", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
        	redirectAttributes.addFlashAttribute("registrationError", e.getMessage());
            return "redirect:/register";
        }
    }
	@GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
