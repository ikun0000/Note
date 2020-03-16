package org.example.api.service;

import feign.hystrix.FallbackFactory;
import org.example.api.entity.Dept;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {
    @Override
    public DeptClientService create(Throwable cause) {
        return new DeptClientService() {
            @Override
            public Dept queryById(long id) {
                return new Dept()
                        .setDeptId(-2)
                        .setDeptName("降级信息：这个服务被关闭了")
                        .setDbSource("service shutdown");
            }

            @Override
            public List<Dept> queryAll() {
                return new ArrayList<Dept>();
            }

            @Override
            public boolean addDept(Dept dept) {
                return false;
            }
        };
    }
}
