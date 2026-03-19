package com.blogplatform.simple_blog_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/posts/*/comments").hasRole("USER")
                .requestMatchers("/", "/register", "/css/**", "/js/**").permitAll()
                .requestMatchers("/posts/**").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .permitAll()
        )
        .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
        );

        return http.build();
    }
}