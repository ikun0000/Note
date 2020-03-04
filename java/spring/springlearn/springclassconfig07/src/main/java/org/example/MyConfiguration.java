package org.example;

import org.example.pojo.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public Student student() {
        Student student = new Student();
        student.setName("test");
        return student;
    }
}
