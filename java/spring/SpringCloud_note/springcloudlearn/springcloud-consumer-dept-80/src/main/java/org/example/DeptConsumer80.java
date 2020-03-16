package org.example;

import org.example.config.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
// 在微服务启动的时候就能去加载自定义的Ribbon类
@RibbonClient(name = "SPRINGCLOUD-PROVIDER-DEPT", configuration = {MyRule.class})
public class DeptConsumer80 {

    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer80.class, args);
    }

}
