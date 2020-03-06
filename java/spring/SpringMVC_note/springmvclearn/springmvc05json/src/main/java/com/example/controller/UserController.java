package com.example.controller;

import com.example.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    @RequestMapping(value = "/j1", produces = "application/json")
    public String json() throws JsonProcessingException {
        User user = new User("嘿嘿", 12, "mail");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        return jsonString;
    }

    @RequestMapping(value = "/j2", produces = "application/json")
    public String json2() throws JsonProcessingException {
        User user1 = new User("aaa", 22, "female");
        User user2 = new User("bbb", 33, "male");
        User user3 = new User("ccc", 44, "female");
        User user4 = new User("ddd", 55, "male");
        User user5 = new User("eee", 66, "female");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);

        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(userList);

        return res;
    }

    @RequestMapping(value = "/j3", produces = "application/json")
    public String json3() throws JsonProcessingException {
        Date date = new Date();
        ObjectMapper objectMapper = new ObjectMapper();
//        User user = objectMapper.readValue(str, User.class);
        return objectMapper.writeValueAsString(date);
    }


}
