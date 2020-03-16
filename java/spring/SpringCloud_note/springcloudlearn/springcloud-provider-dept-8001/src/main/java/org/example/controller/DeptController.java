package org.example.controller;

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

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/add")
    public boolean addDept(Dept dept) {
        return deptService.addDept(dept);
    }

    @GetMapping("/query/{id}")
    public Dept queryById(@PathVariable("id") long id) {
        return deptService.queryById(id);
    }

    @GetMapping("/query")
    public List<Dept> queryAll() {
        return deptService.queryAll();
    }

    // 注册进来的微服务，获取一些消息
    @GetMapping("/dept/discover")
    public Object discovery() {
        // 获取微服务清单
        List<String> services = discoveryClient.getServices();
        System.out.println("discovery -> " + services);

        // 得到具体的微服务信息
        List<ServiceInstance> instances = discoveryClient.getInstances("SPRINGCLOUD-PROVIDER-DEPT");

        for (ServiceInstance instance : instances) {
            System.out.println("Host: " + instance.getHost());
            System.out.println("InstanceId: " + instance.getInstanceId());
            System.out.println("Metadata: " + instance.getMetadata());
            System.out.println("Port: " + instance.getPort());
            System.out.println("Scheme: " + instance.getScheme());
            System.out.println("ServiceId: " + instance.getServiceId());
            System.out.println("Uri: " + instance.getUri());
        }

        return discoveryClient;

    }
}
