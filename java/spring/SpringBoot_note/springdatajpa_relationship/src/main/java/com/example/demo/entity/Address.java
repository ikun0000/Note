package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String phone;
    @Column
    private String zipCode;
    @Column
    private String address;
    @OneToOne(mappedBy = "address")
    private People people;
}
