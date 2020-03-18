package com.example.dao;

import com.example.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findByAuthor(String author);
    List<Article> findByTitleLike(String title);
    List<Article> findByCommentLike(String comment);
}
