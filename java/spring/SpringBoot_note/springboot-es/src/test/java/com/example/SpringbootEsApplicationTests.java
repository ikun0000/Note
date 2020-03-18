package com.example;

import com.example.dao.ArticleRepository;
import com.example.entity.Article;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class SpringbootEsApplicationTests {

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	ArticleRepository articleRepository;

	@Test
	void createIndex() {
		// 根据实体类创建索引
		elasticsearchTemplate.createIndex(Article.class);
	}

	@Test
	void deleteIndex() {
		// 根据实体类删除索引
		elasticsearchTemplate.deleteIndex(Article.class);
	}

	@Test
	public void testMatchQuery() {
		NativeSearchQueryBuilder nativeQueryBuilder = new NativeSearchQueryBuilder();
		nativeQueryBuilder.withQuery(QueryBuilders.matchQuery("title", "HTML"));
		Page<Article> page = articleRepository.search(nativeQueryBuilder.build());

		System.out.println(page.getContent());
	}

	@Test
	public void testMatchPhraseQuery() {
		NativeSearchQueryBuilder nativeQueryBuilder = new NativeSearchQueryBuilder();
		nativeQueryBuilder.withQuery(QueryBuilders.matchPhraseQuery("title", "JAVA教程"));
		Page<Article> page = articleRepository.search(nativeQueryBuilder.build());

		System.out.println(page.getContent());
	}

	@Test
	public void testFuzzyQuery() {
		NativeSearchQueryBuilder nativeQueryBuilder = new NativeSearchQueryBuilder();
		nativeQueryBuilder.withQuery(QueryBuilders.fuzzyQuery("comment", "java"));
		Page<Article> page = articleRepository.search(nativeQueryBuilder.build());

		System.out.println(page.getContent());
	}

	@Test
	public void testTermQuery() {
		NativeSearchQueryBuilder nativeQueryBuilder = new NativeSearchQueryBuilder();
		nativeQueryBuilder.withQuery(QueryBuilders.termQuery("comment", "html"));
		Page<Article> page = articleRepository.search(nativeQueryBuilder.build());

		System.out.println(page.getContent());
	}

	@Test
	public void testRangeQuery() {
		NativeSearchQueryBuilder nativeQueryBuilder = new NativeSearchQueryBuilder();
		Date before = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(before);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		before = calendar.getTime();
		nativeQueryBuilder.withQuery(QueryBuilders.rangeQuery("publishDate").from(before.getTime()).to(new Date().getTime()));
		Page<Article> page = articleRepository.search(nativeQueryBuilder.build());

		System.out.println(page.getContent());
	}
}
