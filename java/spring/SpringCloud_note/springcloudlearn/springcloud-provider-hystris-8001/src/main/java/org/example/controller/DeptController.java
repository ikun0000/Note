package org.example.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.example.api.entity.Dept;
import org.example.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping("/add")
    public boolean addDept(Dept dept) {
        return deptService.addDept(dept);
    }

    @GetMapping("/query/{id}")
    @HystrixCommand(fallbackMethod = "hystrixQueryAll")
    public Dept queryById(@PathVariable("id") long id) {
        Dept dept = deptService.queryById(id);
        if (dept == null) {
            throw new RuntimeException("no id " + id + " dept");
        }
        return dept;
    }

    @GetMapping("/query")
    public List<Dept> queryAll() {
        return deptService.queryAll();
    }

    // queryAll备选方案
    public Dept hystrixQueryAll(long id) {
        return new Dept()
                .setDeptId(-1)
                .setDeptName(id + " is not exists!")
                .setDbSource("not exists");
    }

}
