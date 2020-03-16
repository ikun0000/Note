package org.example.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableEurekaClient
@EnableFeignClients(basePackages = {"org.example.api"})
@ComponentScan("org.example.api")
@Configuration
public class FeignConfig {
}
