package com.example.controller;

import com.example.dto.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("hello")
public class HelloController {

    @GetMapping("/hello")
    @ApiOperation("hello request")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/user")
    @ApiOperation("根据ID返回用户")
    @ApiResponse(code = 200, message = "成功")
    public User user(@ApiParam("用户ID") int id) {
        return new User("aa", "123456", true, false, false, "user");
    }

    @PostMapping("/user")
    public String postUser(@ApiParam("用户ID") int id, @ApiParam("修改用户") User user) {
        return "ok";
    }

    @DeleteMapping("/user")
    public String deleteUser(@ApiParam("用户ID") int id) {
        return "ok";
    }

    @PutMapping("/user")
    public String putUser(@ApiParam("添加的用户") User user) {
        return "ok";
    }

}
