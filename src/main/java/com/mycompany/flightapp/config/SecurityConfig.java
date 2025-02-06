package com.mycompany.flightapp.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/**", "/h2-console/**").permitAll()  // Permit API and H2 Console
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Allow H2 console in iframe
                .httpBasic(withDefaults())
                .build();
    }
}
