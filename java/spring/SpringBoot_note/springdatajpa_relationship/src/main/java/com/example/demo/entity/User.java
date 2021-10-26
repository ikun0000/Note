package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    // 有@JoinTable和@JoinColumn注解的关系维护者的@ManyToMany,@ManyToOne,@OneToMany,@OneToOne不用写mappedBy
    @ManyToMany
    @JoinTable
    private List<Authorty> authorties;
}
