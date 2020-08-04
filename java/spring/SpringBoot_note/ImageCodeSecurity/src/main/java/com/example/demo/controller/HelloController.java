package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String testAccess() {
        return "已经通过认证授权";
    }

    @RequestMapping("/loginpage")
    public String loginPage() {
        return "loginpage";
    }

}
