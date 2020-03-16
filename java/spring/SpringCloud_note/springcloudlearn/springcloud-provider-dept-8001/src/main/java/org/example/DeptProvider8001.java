package org.example;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@MapperScan("org.example.mapper")
@EnableDiscoveryClient      // 服务发现
@EnableEurekaClient         // 注册微服务
public class DeptProvider8001 {

    public static void main(String[] args) {
        SpringApplication.run(DeptProvider8001.class, args);
    }

}