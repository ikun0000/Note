package com.example.service;

import com.example.entity.Books;

import java.util.List;

public interface BookService {
    // 增加一本书
    int addBook(Books books);
    // 删除一本书
    int deleteBookById(int id);
    // 修改一本书
    int updateBook(Books books);
    // 查询一本书
    Books queryById(int id);
    // 查询所有书
    List<Books> findAll();
}
