### SpringBoot整合ES

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
>   * `type`指定索引类型
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



#### 使用`ElasticsearchTemplate`创建和删除索引

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
```

