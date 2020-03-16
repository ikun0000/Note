package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.api.entity.Dept;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeptMapper {
    boolean addDept(Dept dept);
    Dept queryById(@Param("id") long id);
    List<Dept> queryAll();
}
