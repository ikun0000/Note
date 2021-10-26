package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
// https://blog.csdn.net/johnf_nash/article/details/80642581
@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    // 关系维护者，一对多/多对一的关系维护者必须是多的那个，这个类（表）也是被添加外键的
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(referencedColumnName = "id")
    private Author author;
}
