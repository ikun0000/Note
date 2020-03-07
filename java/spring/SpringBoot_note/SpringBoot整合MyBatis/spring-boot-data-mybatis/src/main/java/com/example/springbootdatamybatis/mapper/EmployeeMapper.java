package com.example.springbootdatamybatis.mapper;

import com.example.springbootdatamybatis.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {

    int addEmpl(Employee employee);

    Employee getEmplById(Integer id);

}
