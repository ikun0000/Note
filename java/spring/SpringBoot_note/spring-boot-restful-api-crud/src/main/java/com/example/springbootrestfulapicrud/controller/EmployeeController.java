package com.example.springbootrestfulapicrud.controller;

import com.example.springbootrestfulapicrud.dao.DepartmentDao;
import com.example.springbootrestfulapicrud.dao.EmployeeDao;
import com.example.springbootrestfulapicrud.entities.Department;
import com.example.springbootrestfulapicrud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;


    @GetMapping("/emps")
    public String list(Model model) {
        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("emps", employees);

        return "emp/list";
    }

    @GetMapping("/emp")
    public String toAddPage(Model model) {
        model.addAttribute("deps", departmentDao.getDepartments());
        return "emp/add";
    }

    @PostMapping("/emp")
    public String addEmp(Employee employee) {

        employeeDao.save(employee);
//        System.out.println(employee);
//        System.out.println("保存员工信息");
        // redirect: 表示重定向到一个地方，/表示项目路径
        // forward: 表示转发到一个地址
        return "redirect:/emps";
    }

    @GetMapping("/emp/{id}")
    public String toEditPage(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeDao.get(id);
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("deps", departments);
        model.addAttribute("emp", employee);
        return "emp/add";
    }

    @PutMapping("/emp")
    public String updateEmployee(Employee employee) {
//        System.out.println(employee);
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/emps";
    }

}
