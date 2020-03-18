package com.example.controller;

import com.example.dao.ArticleRepository;
import com.example.dto.ResponseInfo;
import com.example.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @PutMapping("/add")
    public ResponseInfo addAritcle(Article article) {
        article.setPublishDate(new Date());
        Article respArticle = articleRepository.save(article);
        return new ResponseInfo().setResultData(respArticle);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseInfo deleteAritcle(@PathVariable("id") String id) {
        articleRepository.deleteById(id);
        return new ResponseInfo()
                .setCode(1)
                .setMessage("success")
                .setResultData(null);
    }

    @PostMapping("/update")
    public ResponseInfo updateArticle(Article article) {
        Article respArticle = articleRepository.save(article);
        return new ResponseInfo()
                .setCode(1)
                .setMessage("success")
                .setResultData(null);
    }

    @GetMapping("/all")
    public ResponseInfo getAllArticle() {
        Iterable<Article> all = articleRepository.findAll();
        List<Article> articles = new ArrayList<>();
        all.forEach( article -> {articles.add(article); } );
        return new ResponseInfo()
                .setCode(1)
                .setMessage("success")
                .setResultData(articles);
    }

    @GetMapping("/author")
    public ResponseInfo getByAuthor(@RequestParam("author") String author) {
        List<Article> articles = articleRepository.findByAuthor(author);
        return new ResponseInfo()
                .setCode(1)
                .setMessage("success")
                .setResultData(articles);
    }

    @GetMapping("/title")
    public ResponseInfo getByTitle(@RequestParam("title") String title) {
        List<Article> articles = articleRepository.findByTitleLike(title);
        return new ResponseInfo()
                .setCode(1)
                .setMessage("success")
                .setResultData(articles);
    }

    @GetMapping("/comment")
    public ResponseInfo getByComment(@RequestParam("comment") String comment) {
        List<Article> articles = articleRepository.findByCommentLike(comment);
        return new ResponseInfo()
                .setCode(1)
                .setMessage("success")
                .setResultData(articles);
    }

}
