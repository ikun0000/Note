package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class HelloController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @PostMapping("/upload")
    public String upload(HttpServletRequest request,
                         @RequestParam("upload") MultipartFile file,
                         Model model) {
        String path = request.getSession().getServletContext().getRealPath("/upload");
        System.out.println("path: " + path);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        filename = uuid + filename;
        try {
            file.transferTo(new File(dir, filename));
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        model.addAttribute("path", dir + File.separator + filename);
        return "ok";
    }

}
