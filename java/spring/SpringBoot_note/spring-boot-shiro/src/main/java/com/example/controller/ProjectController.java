package com.example.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProjectController {

    @RequestMapping({"/", "/index.html", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/level1/{id}")
    public String level1(@PathVariable("id") int id) {
        return "level1/" + id;
    }

    @RequestMapping("/level2/{id}")
    public String level2(@PathVariable("id") int id) {
        return "level1/" + id;
    }

    @RequestMapping("/level3/{id}")
    public String level3(@PathVariable("id") int id) {
        return "level3/" + id;
    }

    @RequestMapping("/form")
    public String form() {
        return "form";
    }

    @PostMapping("/login")
    public String login(String username, String password, String[] remme, Model model) {
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        // 封装用户登陆数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

        if (remme != null && remme[0].equals("rememberme")) {
            usernamePasswordToken.setRememberMe(true);
        }

        // 执行登陆的方法
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在");
            return "form";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "form";
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }

}
