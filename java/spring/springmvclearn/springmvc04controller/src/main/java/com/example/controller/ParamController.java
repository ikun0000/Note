package com.example.controller;

import com.example.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParamController {

    @GetMapping("/name")
    public String name(@RequestParam("name1") String name) {
        System.out.println(name);
        return "forward:/index.html";
    }

    @GetMapping("/user")
    public String user(User user) {
        System.out.println(user);
        return "forward:/index.html";
    }

}
