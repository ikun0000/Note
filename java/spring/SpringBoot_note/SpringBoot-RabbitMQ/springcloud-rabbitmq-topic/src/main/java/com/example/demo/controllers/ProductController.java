package com.example.demo.controllers;


import com.example.demo.enums.RoutingKeys;
import com.example.demo.provider.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/prod")
public class ProductController {

    @Autowired
    private Provider provider;

    @GetMapping("/eerror")
    public Object error() {
        provider.send(RoutingKeys.PRODUCT_ERROR, "product error");
        HashMap<String, String> map = new HashMap<>();
        map.put("code", "1000");
        map.put("info", "ok");
        map.put("type", "product");
        return map;
    }

    @GetMapping("/info")
    public Object info() {
        provider.send(RoutingKeys.PRODUCT_INFO, "product info");
        HashMap<String, String> map = new HashMap<>();
        map.put("code", "1000");
        map.put("info", "ok");
        map.put("type", "product");
        return map;
    }

}
