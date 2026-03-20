package com.blogplatform.simple_blog_platform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
		logger.warn("Resource not found: {}", ex.getMessage());
		model.addAttribute("errorMessage", ex.getMessage());
		 return "error/404";
	}
	
	public String handlerGenericException(Exception ex, Model model)
	{
		logger.error("An unexpected error occurred", ex);
		model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
		return "error/generic-error";
	}
}
