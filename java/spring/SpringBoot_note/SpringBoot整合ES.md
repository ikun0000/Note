# SpringBoot整合ES

#### 首先导入es的starter

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```



#### 配置属性

```yaml
spring:
  data:
    elasticsearch:
      cluster-name: docker-cluster			# ES集群名称
      cluster-nodes: 10.10.10.246:9300		# 逗号分隔的集群机器
```

> 注意这里的端口是9300的数据端口，不是9200

随着ES和spring-data-elasticsearch版本的迭代，上面的配置可能不能用了，如果测试是出现以下异常

```
org.springframework.dao.DataAccessResourceFailureException: Timeout connecting to [localhost/127.0.0.1:9200]; nested exception is java.lang.RuntimeException: Timeout connecting to [localhost/127.0.0.1:9200]
```

那么上面的配置失效，需要自己配置 `RestHighLevelClient` ，如下

```java
package com.example.demo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ESConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration
                .builder()
                .connectedTo("10.10.10.246:9200")	// 设置ES的地址，端口是9200
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}

```



#### 设计实体类

```java
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
```

> * `@Document`用于标注一个实体类
>   * `indexName`指定索引名
>   * ~~`type`指定索引类型~~
>   * `shards`指定分片数量，默认为5
>   * `replicas`指定备份数量，默认为1
> * `@Id`指定一个字段为文档ID
> * `@Field`制定字段类型和名称
>   * `name`指定字段名称
>   * `type`指定字段类型
>   *  `index`指定该字段是否为索引
>   * ` store `指定该字段是否存储 
>   *  `analyzer`指定分词器名称  
>   * ` format `自定义日期的格式



#### ~~使用`ElasticsearchTemplate`创建和删除索引~~

```java
@SpringBootTest
class SpringbootEsApplicationTests {

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

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
}
```



#### 开发Article CRUD接口

接口返回信息类定义：

```java
@Data
@Accessors(chain = true)
public class ResponseInfo {
    private Integer code;
    private String message;
    private Object resultData;
}
```



开发CRUD接口

> SpringData ElasticSearch进行CRUD的方式和SpringData JPA类似，写一个接口然后继承一个接口并指定实体类和ID字段类型就可以实现简单的CRUD操作
>
> 接口关系：
>
> ```
> Repository
> 	^
> 	|
> CrudRepository
> 	^
> 	|
> PagingAndSortingRepository
> 	^
> 	|
> ElasticsearchCrudRepository
> 	^
> 	|
> ElasticsearchRepository
> ```

```java
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findByAuthor(String author);
    List<Article> findByTitleLike(String title);
    List<Article> findByCommentLike(String comment);
}
```

> 要实现复杂查询只需要按照命名规则命名就可以实现复杂查询了

 **Supported keywords inside method names**

| Keyword               | Sample                               | Elasticsearch Query String                                   |
| :-------------------- | :----------------------------------- | :----------------------------------------------------------- |
| `And`                 | `findByNameAndPrice`                 | `{"bool" : {"must" : [ {"field" : {"name" : "?"}}, {"field" : {"price" : "?"}} ]}}` |
| `Or`                  | `findByNameOrPrice`                  | `{"bool" : {"should" : [ {"field" : {"name" : "?"}}, {"field" : {"price" : "?"}} ]}}` |
| `Is`                  | `findByName`                         | `{"bool" : {"must" : {"field" : {"name" : "?"}}}}`           |
| `Not`                 | `findByNameNot`                      | `{"bool" : {"must_not" : {"field" : {"name" : "?"}}}}`       |
| `Between`             | `findByPriceBetween`                 | `{"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : ?,"include_lower" : true,"include_upper" : true}}}}}` |
| `LessThanEqual`       | `findByPriceLessThan`                | `{"bool" : {"must" : {"range" : {"price" : {"from" : null,"to" : ?,"include_lower" : true,"include_upper" : true}}}}}` |
| `GreaterThanEqual`    | `findByPriceGreaterThan`             | `{"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : null,"include_lower" : true,"include_upper" : true}}}}}` |
| `Before`              | `findByPriceBefore`                  | `{"bool" : {"must" : {"range" : {"price" : {"from" : null,"to" : ?,"include_lower" : true,"include_upper" : true}}}}}` |
| `After`               | `findByPriceAfter`                   | `{"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : null,"include_lower" : true,"include_upper" : true}}}}}` |
| `Like`                | `findByNameLike`                     | `{"bool" : {"must" : {"field" : {"name" : {"query" : "?*","analyze_wildcard" : true}}}}}` |
| `StartingWith`        | `findByNameStartingWith`             | `{"bool" : {"must" : {"field" : {"name" : {"query" : "?*","analyze_wildcard" : true}}}}}` |
| `EndingWith`          | `findByNameEndingWith`               | `{"bool" : {"must" : {"field" : {"name" : {"query" : "*?","analyze_wildcard" : true}}}}}` |
| `Contains/Containing` | `findByNameContaining`               | `{"bool" : {"must" : {"field" : {"name" : {"query" : "**?**","analyze_wildcard" : true}}}}}` |
| `In`                  | `findByNameIn(Collectionnames)`      | `{"bool" : {"must" : {"bool" : {"should" : [ {"field" : {"name" : "?"}}, {"field" : {"name" : "?"}} ]}}}}` |
| `NotIn`               | `findByNameNotIn(Collectionnames)`   | `{"bool" : {"must_not" : {"bool" : {"should" : {"field" : {"name" : "?"}}}}}}` |
| `Near`                | `findByStoreNear`                    | `Not Supported Yet !`                                        |
| `True`                | `findByAvailableTrue`                | `{"bool" : {"must" : {"field" : {"available" : true}}}}`     |
| `False`               | `findByAvailableFalse`               | `{"bool" : {"must" : {"field" : {"available" : false}}}}`    |
| `OrderBy`             | `findByAvailableTrueOrderByNameDesc` | `{"sort" : [{ "name" : {"order" : "desc"} }],"bool" : {"must" : {"field" : {"available" : true}}}}` |

如果不能满足要求可以使用`@Query`注解写查询条件

```java
interface BookRepository extends ElasticsearchRepository<Book, String> {
    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"name\" : \"?0\"}}}}")
    Page<Book> findByName(String name,Pageable pageable);
}
```

> 实现分页和SpringData JPA也是一样，在定义的方法参数最后加上`Pageable`参数



实现CURD接口

```java
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
```

> 用法和SpringData JPA差不多，其实findAll()返回的应该是一个Page对象



### 原生方法查询

继承了`ElasticsearchRepository`会有一个search方法，该方法传入一个`QueryBuilder`对象，使用`NativeSearchQueryBuilder`可以构建类似于原生HTTP的查询，`QueryBuilders`用于构建不同的查询，例如：match，multi_match，match_phrase，term等RESTful的查询

`ElasticsearchRepository`也可以添加 聚合 过滤器 。。。

```java
@Autowired
ArticleRepository articleRepository;

@Test
public void testMatchQuery() {
    NativeSearchQueryBuilder nativeQueryBuilder = new NativeSearchQueryBuilder();
    nativeQueryBuilder.withQuery(QueryBuilders.matchQuery("title", "HTML"));
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
```



#### `ElasticsearchRepository`

`ElasticsearchRepository`和使用RESTful查询时的request body差不都是一个东西

举几个例子

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"match": {
			"author": "eeee"
		}
	}
}'
```

相当于

```java
NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("author", "eeee"));
articleRepository.search(nativeSearchQueryBuilder.build());
```

> RESTful对应的`ElasticsearchRepository`方法
>
> | RESTful | ElasticsearchRepository |
> | ------- | ----------------------- |
> | query       | withQuery  |
> | aggs | addAggregation |
>
> 
>
> | RESTful      | QueryBuilders |
> | ------------ | ------------- |
> | match_all | QueryBuilders.matchAllQuery()... |
> | match        | QueryBuilders.matchQuery(String name, Object text)... |
> | match_phrase | QueryBuilders.matchPhraseQuery(String name, Object text)... |
> | multi_match  | QueryBuilders.multiMatchQuery(Object text, String... fieldNames)... |
> | query_string | QueryBuilders.queryStringQuery(String queryString).field(String field)... |
> | term         | QueryBuilders.termQuery(String name, Object value)... |
> | range        | QueryBuilders.rangeQuery(String name)... |
> | bool         | QueryBuilders.boolQuery().must()... |
>
> 
>



# 解决问题

在导入spring-boot-starter-data-elasticsearch时先到pom.xml中点击 `<artifactId>spring-boot-starter-parent</artifactId>` 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.3.1.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example</groupId>
	<artifactId>newdemo</artifactId>
	<version>0.0.1-SNAPSHOT
        ...
```

然后跳到

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.3.1.RELEASE</version>
  </parent>
  <artifactId>spring-boot-starter-parent</artifactId>
  <packaging>pom</packaging>
  <name>spring-boot-starter-parent</name>
  <description>Parent pom providing dependency and plugin management for applications built with Maven</description>
  <properties>
    <java.version>1.8</java.version>
    <resource.delimiter>@</resource.delimiter>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <maven.compiler.target>${java.version}</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties>
    ...
```

在点击 `<artifactId>spring-boot-dependencies</artifactId>` 跳到

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-dependencies</artifactId>
  <version>2.3.1.RELEASE</version>
  <packaging>pom</packaging>
  <name>spring-boot-dependencies</name>
  <description>Spring Boot Dependencies</description>
  <url>https://spring.io/projects/spring-boot</url>
  <organization>
    <name>Pivotal Software, Inc.</name>
    <url>https://spring.io</url>
  </organization>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0</url>
    </license>
  </licenses>
  <developers>
    <developer>
      <name>Pivotal</name>
      <email>info@pivotal.io</email>
      <organization>Pivotal Software, Inc.</organization>
      <organizationUrl>https://www.spring.io</organizationUrl>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:git://github.com/spring-projects/spring-boot.git</connection>
    <developerConnection>scm:git:ssh://git@github.com/spring-projects/spring-boot.git</developerConnection>
    <url>https://github.com/spring-projects/spring-boot</url>
  </scm>
  <issueManagement>
    <system>GitHub</system>
    <url>https://github.com/spring-projects/spring-boot/issues</url>
  </issueManagement>
  <properties>
    <activemq.version>5.15.12</activemq.version>
    <antlr2.version>2.7.7</antlr2.version>
    <appengine-sdk.version>1.9.80</appengine-sdk.version>
    <artemis.version>2.12.0</artemis.version>
    <aspectj.version>1.9.5</aspectj.version>
    <assertj.version>3.16.1</assertj.version>
    <atomikos.version>4.0.6</atomikos.version>
    <awaitility.version>4.0.3</awaitility.version>
    <bitronix.version>2.1.4</bitronix.version>
    <build-helper-maven-plugin.version>3.1.0</build-helper-maven-plugin.version>
    <byte-buddy.version>1.10.11</byte-buddy.version>
    <caffeine.version>2.8.4</caffeine.version>
    <cassandra-driver.version>4.6.1</cassandra-driver.version>
    <classmate.version>1.5.1</classmate.version>
    <commons-codec.version>1.14</commons-codec.version>
    <commons-dbcp2.version>2.7.0</commons-dbcp2.version>
    <commons-lang3.version>3.10</commons-lang3.version>
    <commons-pool.version>1.6</commons-pool.version>
    <commons-pool2.version>2.8.0</commons-pool2.version>
    <couchbase-client.version>3.0.5</couchbase-client.version>
    <db2-jdbc.version>11.5.0.0</db2-jdbc.version>
    <dependency-management-plugin.version>1.0.9.RELEASE</dependency-management-plugin.version>
    <derby.version>10.14.2.0</derby.version>
    <dropwizard-metrics.version>4.1.9</dropwizard-metrics.version>
    <ehcache.version>2.10.6</ehcache.version>
    <ehcache3.version>3.8.1</ehcache3.version>
    <elasticsearch.version>7.6.2</elasticsearch.version>
      ...
```

看到 `<elasticsearch.version>7.6.2</elasticsearch.version>` ，也就是elasticsearch的客户端使用的是7.6.2版本，要么在项目的pom.xml中的`properties` 指定现在用的es客户端的版本（和ES服务的版本一样），要么安装这个版本的ES服务

```xml
<properties>
    <java.version>1.8</java.version>
    <elasticsearch.version>7.8.1</elasticsearch.version>
</properties>
```

