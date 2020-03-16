package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer         // 开启Eureka Server
public class EurekaServer7001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer7001.class, args);
    }

}
