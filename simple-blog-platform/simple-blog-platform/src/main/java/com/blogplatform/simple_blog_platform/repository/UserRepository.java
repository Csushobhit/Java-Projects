package com.blogplatform.simple_blog_platform.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blogplatform.simple_blog_platform.model.User;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
