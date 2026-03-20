package com.blogplatform.simple_blog_platform.dto;

import jakarta.validation.constraints.NotEmpty;

public class CommentDto {
	
	@NotEmpty(message = "Comment content cannot be empty")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
