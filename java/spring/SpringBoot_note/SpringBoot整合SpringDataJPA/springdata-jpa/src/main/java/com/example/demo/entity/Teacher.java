package com.example.demo.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "teacher")
@Data
public class Teacher {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Integer age;
}
