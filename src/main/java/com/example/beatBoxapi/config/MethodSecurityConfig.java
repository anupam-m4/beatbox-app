package com.example.beatBoxapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

// This enables the use of @PreAuthorize and @PostAuthorize annotations
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
}