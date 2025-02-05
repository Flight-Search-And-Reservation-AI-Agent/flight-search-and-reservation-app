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
                // Use the lambda DSL to disable CSRF
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Permit requests to public endpoints
                        .requestMatchers("/api/v1/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .build();
    }
}


