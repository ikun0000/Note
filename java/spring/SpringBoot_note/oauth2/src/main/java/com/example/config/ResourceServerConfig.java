package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Order(2)
@Configuration
@EnableResourceServer
public class ResourceServerConfig {
}
