package com.example.dao;

import com.example.entity.Books;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper {
    // 增加一本书
    int addBook(Books books);
    // 删除一本书
    int deleteBookById(@Param("bookId") int id);
    // 修改一本书
    int updateBook(Books books);
    // 查询一本书
    Books queryById(@Param("bookId") int id);
    // 查询所有书
    List<Books> findAll();
}
