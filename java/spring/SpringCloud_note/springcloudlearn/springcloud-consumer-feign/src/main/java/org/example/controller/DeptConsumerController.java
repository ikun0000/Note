package org.example.controller;

import org.example.api.entity.Dept;
import org.example.api.service.DeptClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class DeptConsumerController {

    @Autowired
    private DeptClientService deptClientService;

    @GetMapping("/dept/{id}")
    public Dept get(@PathVariable("id") long id) {
        return deptClientService.queryById(id);
    }

    @GetMapping("/dept")
    public List<Dept> getAll() {
        return deptClientService.queryAll();
    }

    @PostMapping("/dept/add")
    public boolean add(Dept dept) {
        return deptClientService.addDept(dept);
    }

}
