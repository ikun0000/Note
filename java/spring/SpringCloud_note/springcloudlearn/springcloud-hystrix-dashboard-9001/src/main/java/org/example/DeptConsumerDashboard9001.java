package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class DeptConsumerDashboard9001 {

    public static void main(String[] args) {
        SpringApplication.run(DeptConsumerDashboard9001.class, args);
    }

}
