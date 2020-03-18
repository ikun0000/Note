package com.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "blog", type = "article", shards = 3, replicas = 0)
public class Article {
    /**
     * 文档ID
     */
    @Id
    String id;

    /**
     * 文章的作者
     */
    @Field(name = "author", type = FieldType.Keyword)
    String author;

    /**
     * 文章的标题
     */
    @Field(name = "title", type = FieldType.Text)
    String title;

    /**
     * 文章的内容
     */
    @Field(name = "comment", type = FieldType.Text)
    String comment;

    /**
     * 文章的发布日期
     */
    @Field(name = "publish_date", type = FieldType.Date, format = DateFormat.basic_date)
    Date publishDate;
}
