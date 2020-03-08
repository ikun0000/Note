package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        // 配置页面信息
        docket.apiInfo(new ApiInfo("Hello",
                "hello desc",
                "1.0",
                "none",
                new Contact("aa", "aa.com", "aa@qq.com"),
                "MIT",
                "",
                new ArrayList<VendorExtension>()))
                // 配置显示哪些接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
//                .apis(RequestHandlerSelectors.basePackage("com.example.dto"))
                // 过滤路径
//                .paths((str) -> { return str.startsWith("/admin"); })
                .build()
                // 控制是否启动swagger
                .enable(true)
                // 配置组名，多个Docket时使用，每个Docket Bean就是一个组
                .groupName("Test");


        return docket;
    }

}
