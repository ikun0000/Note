package com.example.demo.controllers;


import com.example.demo.provider.ErrorProvider;
import com.example.demo.provider.InfoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TestController {

    @Autowired
    private InfoProvider infoProvider;

    @Autowired
    private ErrorProvider errorProvider;

    @GetMapping("/eerror")
    public Object error() {
        errorProvider.send("get error mapping");
        HashMap<String, String> map = new HashMap<>();
        map.put("code", "1000");
        map.put("info", "ok");
        return map;
    }

    @GetMapping("/info")
    public Object info() {
        infoProvider.send("get info mapping");
        HashMap<String, String> map = new HashMap<>();
        map.put("code", "1000");
        map.put("info", "ok");
        return map;
    }

}
