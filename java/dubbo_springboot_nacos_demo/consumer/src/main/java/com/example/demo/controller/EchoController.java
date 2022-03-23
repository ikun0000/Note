package com.example.demo.controller;

import common.api.Echo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

    @DubboReference(check = false,
            timeout = 3000,
            methods = {@Method(name = "echo", retries = 2)},
            version = "v2",
            interfaceClass = Echo.class,
            stub = "com.example.demo.service.EchoServiceStub",
            loadbalance = "roundrobin",
            cache = "lru")
    private Echo echo;

    @GetMapping("/echo/{data}")
    public String echo(@PathVariable("data") String data) {
        return echo.echo(data);
    }
}
