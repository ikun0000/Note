package org.example.controller;

import org.example.api.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class DeptConsumerController {

    @Autowired
    private RestTemplate restTemplate;

//    public static final String REST_URL_PREFIX = "http://localhost:8001";
    public static final String REST_URL_PREFIX = "http://SPRINGCLOUD-PROVIDER-DEPT";

    @GetMapping("/dept/{id}")
    public Dept get(@PathVariable("id") long id) {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/query/" + id,
                Dept.class);
    }

    @GetMapping("/dept")
    public List<Dept> getAll() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/query",
                List.class);
    }

    @PostMapping("/dept/add")
    public boolean add(Dept dept) {
        return restTemplate.postForObject(REST_URL_PREFIX + "/dept/add",
                dept,
                Boolean.class);
    }

}
