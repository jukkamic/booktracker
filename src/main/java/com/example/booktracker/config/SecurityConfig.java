package com.example.booktracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Book Tracker application.
 * Defines USER and ADMIN roles with appropriate access controls.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure security filter chain with role-based access control.
     * 
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/books/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/edit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login") // The GET route (Where the form lives)
                        .loginProcessingUrl("/perform_login") // The POST route (Handled by Spring)
                        .defaultSuccessUrl("/", true) // Where to go after success
                        .permitAll() // CRITICAL: Makes the login logic public
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/"));

        return http.build();
    }

    /**
     * Configure user details service with in-memory users.
     * Creates two users: one with USER role and one with ADMIN role.
     * 
     * @return the configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user123") // {noop} indicates no encoding (for demo purposes)
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin123") // {noop} indicates no encoding (for demo purposes)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}