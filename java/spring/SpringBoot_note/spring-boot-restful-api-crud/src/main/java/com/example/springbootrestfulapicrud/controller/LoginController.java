package com.example.springbootrestfulapicrud.controller;


import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> msg,
                        HttpSession httpSession) {

        if (!StringUtils.isEmpty(username) && "123456".equals(password)) {
            httpSession.setAttribute("loginuser", username);
            return "redirect:/main.html";
        } else {
            msg.put("msg", "用户名密码错误");
            return "index";
        }
    }

}
