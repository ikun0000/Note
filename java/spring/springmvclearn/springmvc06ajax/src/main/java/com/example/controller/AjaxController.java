package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxController {

    @RequestMapping(value = "/t1", produces = "application/json")
    public String t1() {
        return "hello";
    }

}
