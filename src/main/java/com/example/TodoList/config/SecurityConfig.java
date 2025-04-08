package com.example.TodoList.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Allow H2 console in development
                        .anyRequest().authenticated()
                )
                .httpBasic() // Enable basic authentication
                .and()
                .csrf().disable() // Disable CSRF for simplicity (enable in production)
                .headers().frameOptions().disable(); // Allow frames for H2 console

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Create test users (in production, use a database)
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("password1"))
                .roles("USER")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder().encode("password2"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}