package com.example.springbootdatamybatis.controller;


import com.example.springbootdatamybatis.entity.Employee;
import com.example.springbootdatamybatis.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empl")
public class EmployeeController {

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/query/{id}")
    public Employee queryEmpl(@PathVariable("id") Integer id) {
        return employeeMapper.getEmplById(id);
    }

    @GetMapping("/add")
    public Employee addEmpl(Employee employee) {
        employeeMapper.addEmpl(employee);
        return employee;
    }

}
