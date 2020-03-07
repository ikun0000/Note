package com.example.demo.controller;


import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Consumer;

@RestController
public class HelloController {

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping("/")
    public List<Teacher> hello() {
        return teacherRepository.findByNameStartingWithAndAgeLessThan("test", 22);
    }

    @GetMapping("/save")
    public Map<String, Object> addTeacher(@RequestParam(value = "name", required = true) String name,
                                          @RequestParam(value = "age", required = true) Integer age) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (name.equals("")) {
            hashMap.put("code", 2000);
            hashMap.put("message", "add fail");
            return hashMap;
        }
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setAge(age);
        teacherRepository.save(teacher);
        hashMap.put("code", 1000);
        hashMap.put("message", "success");
        return hashMap;
    }

}
