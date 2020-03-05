package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ModelTest1 {

    @GetMapping("/m1/t1")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getSession().getId());
    }

    @GetMapping("/m1/t2")
    public String t2() {
        return "forward:/index.html";
    }

    @GetMapping("/t3")
    public String t3() {
        return "redirect:/hello";
    }

}
