package org.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigClientController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServer;

    @Value("${server.port}")
    private Integer serverPort;


    @GetMapping("/config/get")
    public Map<String, Object> getConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("applicationName", applicationName);
        map.put("eurekaServer", eurekaServer);
        map.put("serverPort", serverPort);
        return map;
    }

}
