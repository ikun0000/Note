package com.example.springbootdatamybatis.mapper;

import com.example.springbootdatamybatis.entity.Department;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface DepartmentMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into department(departmentName) values(#{departmentName}) ")
    int addDept(Department department);

    @Select("select * from department where id=#{id}")
    Department getDeptById(Integer id);
}
