package com.example.controller;

import com.example.dto.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
public class RestfulController {

    @GetMapping("/item")
    public Person item() {
        Person p = new Person();
        p.setName("testname");
        p.setAge(22);
        p.setBirthday(new Date());
        return p;
    }

}
