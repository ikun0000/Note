package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormController {

    @PostMapping("/testcode")
    public String testcode(@RequestParam("usernm") String usernm,
                           @RequestParam("passwd") String passwd) {
        System.out.println(usernm);
        System.out.println(passwd);

        return "success";
    }
}
