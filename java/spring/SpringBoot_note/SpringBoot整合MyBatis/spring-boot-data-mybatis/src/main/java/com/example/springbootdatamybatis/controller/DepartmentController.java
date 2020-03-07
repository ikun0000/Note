package com.example.springbootdatamybatis.controller;

import com.example.springbootdatamybatis.entity.Department;
import com.example.springbootdatamybatis.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dept")
public class DepartmentController {

    @Autowired
    DepartmentMapper departmentMapper;

    @GetMapping("/query/{id}")
    public Department queryDept(@PathVariable("id") Integer id) {
        return departmentMapper.getDeptById(id);
    }

    @GetMapping("/add")
    public Department addDept(Department department) {
        departmentMapper.addDept(department);
        return department;
    }
}
