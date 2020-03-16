package org.example.api.service;

import org.example.api.entity.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@FeignClient(value = "SPRINGCLOUD-PROVIDER-DEPT", fallbackFactory = DeptClientServiceFallbackFactory.class)
public interface DeptClientService {
    @GetMapping("/dept/query/{id}")
    Dept queryById(@PathVariable("id") long id);
    @GetMapping("/dept/query")
    List<Dept> queryAll();
    @PostMapping("/dept/add")
    boolean addDept(Dept dept);
}
