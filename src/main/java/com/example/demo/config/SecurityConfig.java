package com.example.demo.config;

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

/**
 * Security configuration for the application.
 * Configures form-based authentication with demo user credentials.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws RuntimeException if an error occurs during configuration
     */
    @Bean
    @SuppressWarnings({"PMD.SignatureDeclareThrowsException", "PMD.LambdaCanBeMethodReference"})
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/**").permitAll()  // Allow H2 Console without auth
                .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/tasks", true)  // Redirect to tasks after login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")  // Disable CSRF for H2 Console
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())  // Allow H2 Console frames
            );

        return http.build();
    }

    /**
     * Configures in-memory user details for demo purposes.
     *
     * @return UserDetailsService with demo user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails demoUser = User.builder()
            .username("demo")
            .password(passwordEncoder().encode("demo123"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(demoUser);
    }

    /**
     * Configures password encoder.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
