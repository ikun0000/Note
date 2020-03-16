package org.example.service;

import org.example.api.entity.Dept;

import java.util.List;

public interface DeptService {
    boolean addDept(Dept dept);
    Dept queryById(long id);
    List<Dept> queryAll();
}
