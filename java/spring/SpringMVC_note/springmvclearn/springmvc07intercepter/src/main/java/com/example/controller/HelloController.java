package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {

    @PostMapping("/login")
    public String login(@RequestParam("usernm") String usernm,
                        @RequestParam("passwd") String passwd,
                        HttpServletRequest request) {
        if (usernm.equals("admin") && passwd.equals("123456")) {
            request.getSession().setAttribute("usernm", usernm);
            return "success";
        }
        return "fail";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

}
