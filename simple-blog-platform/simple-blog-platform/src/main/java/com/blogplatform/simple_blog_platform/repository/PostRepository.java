package com.blogplatform.simple_blog_platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogplatform.simple_blog_platform.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
		Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
