package org.example.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCircuitBreaker
public class HystrixConfig {
    @Bean
    public ServletRegistrationBean hystrixMetricsStreamServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new HystrixMetricsStreamServlet());
        bean.addUrlMappings("/actuator/hystrix.stream");       // 访问的监控页面
        return bean;
    }
}
