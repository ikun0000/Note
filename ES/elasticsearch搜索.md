# ElasticSearch搜索



## 空搜索

搜索API的最基础的形式是没有指定任何查询的空搜索，它简单地返回集群中所有索引下的所有文档

```http
GET /_search
```

response

```json
{
  "took": 25,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1,
      "relation": "eq"
    },
    "max_score": 1.0,
    "hits": [
      {
        "_index": "website",
        "_type": "blog",
        "_id": "82zxxnMBmvSkMRE8jGqQ",
        "_score": 1.0,
        "_source": {
          "title": "My second blog entry",
          "text": "Still trying this out...",
          "date": "2014/01/01"
        }
      }
    ]
  }
}
```

### 查询返回值说明

* **hits**

  返回结果中最重要的部分是 `hits` ，它包含 `total` 字段来表示匹配到的文档总数，并且一个 `hits` 数组包含所查询结果的前十个文档。在 `hits` 数组中每个结果包含文档的 `_index` 、 `_type` 、 `_id` ，加上 `_source` 字段。这意味着我们可以直接从返回的搜索结果中使用整个文档。每个结果还有一个 `_score` ，它衡量了文档与查询的匹配程度。默认情况下，首先返回最相关的文档结果，就是说，返回的文档是按照 `_score` 降序排列的。

  `max_score` 值是与查询所匹配文档的 `_score` 的最大值。

* **took**

  `took` 值告诉我们执行整个搜索请求耗费了多少毫秒。 

* **shards**

  `_shards` 部分告诉我们在查询中参与分片的总数，以及这些分片成功了多少个失败了多少个。正常情况下我们不希望分片失败，但是分片失败是可能发生的。如果我们遭遇到一种灾难级别的故障，在这个故障中丢失了相同分片的原始数据和副本，那么对这个分片将没有可用副本来对搜索请求作出响应。假若这样，Elasticsearch 将报告这个分片是失败的，但是会继续返回剩余分片的结果。

* **timeout**

  `timed_out` 值告诉我们查询是否超时。默认情况下，搜索请求不会超时。

  > 如果响应时间比查询结果更重要，你可以指定 `timeout` 为 10 或者 10ms（10毫秒），或者 1s（1秒）： 
  >
  > ```http
  > GET /_search?timeout=10ms
  > ```



## 多索引多类型搜索
ES还提供跨越索引和跨越类型的搜索方式，他的URI形式如下

- **`/_search`**

  在所有的索引中搜索所有的类型

- **`/gb/_search`**

  在 `gb` 索引中搜索所有的类型

- **`/gb,us/_search`**

  在 `gb` 和 `us` 索引中搜索所有的文档

- **`/g\*,u\*/_search`**

  在任何以 `g` 或者 `u` 开头的索引中搜索所有的类型

- **`/gb/user/_search`**

  在 `gb` 索引中搜索 `user` 类型

- **`/gb,us/user,tweet/_search`**

  在 `gb` 和 `us` 索引中搜索 `user` 和 `tweet` 类型

- **`/_all/user,tweet/_search`**

  在所有的索引中搜索 `user` 和 `tweet` 类型




## 结构化搜索

*结构化搜索（Structured search）* 是指有关探询那些具有内在结构数据的过程。

把查询条件放到 `filter` 里面可以加快查询速度，也可以被缓存

```sql
{
	"query": {
		"constant_score": {
			"filter": {
				...
			}
		}
	}
}
```



### 查找精确的值

当进行精确值查找时， 我们会使用过滤器（filters）。过滤器很重要，因为它们执行速度非常快，不会计算相关度（直接跳过了整个评分阶段）而且很容易被缓存。

#### term查询

>  term查询可以用来处理数字（numbers）、布尔值（Booleans）、日期（dates）以及文本（text）。 

```http
GET /my_store/products/_search
{
    "query" : {
        "constant_score" : { 
            "filter" : {
                "term" : { 
                    "price" : 20
                }
            }
        }
    }
}
```

`constant_score` 将 `term` 查询转化成为过滤器，类似的SQL如下

```sql
SELECT document
FROM   products
WHERE  price = 20
```

查询文本如下

```http
GET /my_store/products/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "term" : {
                    "productID" : "XHDK-A-1293-#fJ3"
                }
            }
        }
    }
}
```

当然也可以不转化为过滤器

```json
{
    "query": {
        "term": {
            "title": "demo"
        }
    }
}
```



### 查询多个精确值

`term` 只能查询一个值，`terms` 则可以一次匹配多个值（***末尾有s***）

```sql
WHERE price in (20, 30)
WHERE price = 20 OR price = 30
```

相当于

```json
{
    "terms" : {
        "price" : [20, 30]
    }
}
```

`terms` 也可以放在 `filter` 中

```http
GET /my_store/products/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "terms" : { 
                    "price" : [20, 30]
                }
            }
        }
    }
}
```



>  `term` 和 `terms` 是 *包含（contains）* 操作，而非 *等值（equals）* （判断）
>
> 如果我们有一个 term（词项）过滤器 `{ "term" : { "tags" : "search" } }` ，它会与以下两个文档 *同时* 匹配：
>
> ```json
> { "tags" : ["search"] }
> { "tags" : ["search", "open_source"] } 
> ```
>
> 也就是说 `term` 和 `terms` 类似于SQL中`{field} = {value}` 和 `IN (...)` 的语法



### 组合过滤器

组合过滤器相当于SQL中使用 `WHERE`, `AND`, `OR` 等关键字

如这条SQL

```sql
SELECT product
FROM   products
WHERE  (price = 20 OR productID = "XHDK-A-1293-#fJ3")
  AND  (price != 30)
```

可以使用布尔过滤器来组装

#### 布尔过滤器

一个 `bool` 过滤器由三部分组成：

```json
{
   "bool" : {
      "must" :     [],
      "should" :   [],
      "must_not" : [],
   }
}
```

* **`must`**

  所有的语句都 *必须（must）* 匹配，与 `AND` 等价

* **`must_not`**

  所有的语句都 *不能（must not）* 匹配，与 `NOT` 等价

* **`should`**

  至少有一个语句要匹配，与 `OR` 等价

上面的SQL如下

```http
GET /my_store/products/_search
{
   "query" : {
      "filtered" : { 
         "filter" : {
            "bool" : {
              "should" : [
                 { "term" : {"price" : 20}}, 
                 { "term" : {"productID" : "XHDK-A-1293-#fJ3"}} 
              ],
              "must_not" : {
                 "term" : {"price" : 30} 
              }
           }
         }
      }
   }
}
```

当然可以不用`filtered` 和 `filter` 抱起来，也就是直接放在 `query` 之下



#### 嵌套布尔过滤器

```sql
SELECT document
FROM   products
WHERE  productID      = "KDKE-B-9947-#kL5"
  OR (     productID = "JODL-X-1937-#pV7"
       AND price     = 30 )
```

```http
GET /my_store/products/_search
{
   "query" : {
      "filtered" : {
         "filter" : {
            "bool" : {
              "should" : [
                { "term" : {"productID" : "KDKE-B-9947-#kL5"}}, 
                { "bool" : { 
                  "must" : [
                    { "term" : {"productID" : "JODL-X-1937-#pV7"}}, 
                    { "term" : {"price" : 30}} 
                  ]
                }}
              ]
           }
         }
      }
   }
}
```



### 查询一个范围的值

Elasticsearch 有 `range` 查询，不出所料地，它用来查找处于某个范围内的文档，比如像下面的SQL语句

```sql
SELECT document
FROM   products
WHERE  price BETWEEN 20 AND 40
```

在ES中为

```json
"range" : {
    "price" : {
        "gte" : 20,
        "lte" : 40
    }
}
```

- `gt`: `>` 大于（greater than）
- `lt`: `<` 小于（less than）
- `gte`: `>=` 大于或等于（greater than or equal to）
- `lte`: `<=` 小于或等于（less than or equal to）

如果想要范围无界（比方说 >20 ），只需要包含一个范围即可

```json
"range" : {
    "price" : {
        "gt" : 20
    }
}
```

日期使用字符串表示

```json
"range" : {
    "timestamp" : {
        "gt" : "2014-01-01 00:00:00",
        "lt" : "2014-01-07 00:00:00"
    }
}
```

 `range` 查询支持对 *日期（date math）* 进行操作，比方说，如果我们想查找过去一小时内的所有文档：

```json
"range" : {
    "timestamp" : {
        "gt" : "now-1h"
    }
}
```

也可以制定一个时间点的之后一小时，只需要在时间后面加上 `||` 然后再写上一个日期数字表达式

```json
"range" : {
    "timestamp" : {
        "gt" : "2014-01-01 00:00:00",
        "lt" : "2014-01-01 00:00:00||+1M" 
    }
}
```

除了日期时间和数字以外还可以对字符串比较

```json
"range" : {
    "title" : {
        "gte" : "a",
        "lt" :  "b"
    }
}
```



### 匹配NULL值

SQL中 `IS NULL` 和 `IS NOT NULL` 的语法在ES中是`"missing": {"field": {value}}` 和 `"exists": {"field": {value}}` 。

SQL中有查询非NULL的字段的语法

```sql
SELECT tags
FROM   posts
WHERE  tags IS NOT NULL
```

ES也有查询非NULL值得方法

```http
GET /my_index/posts/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "exists" : { "field" : "tags" }
            }
        }
    }
}
```



亦或者查找某个字段为NULL的内容

```sql
SELECT tags
FROM   posts
WHERE  tags IS NULL
```

ES的写法

```http
GET /my_index/posts/_search
{
    "query" : {
        "constant_score" : {
            "filter": {
                "missing" : { "field" : "tags" }
            }
        }
    }
}
```





## 全文搜索

全文搜索两个最重要的概念

* **相关性（Relevance）**
* **分析（Analysis）**



### 匹配查询

匹配查询 `match` 是个 *核心* 查询。无论需要查询什么字段， `match` 查询都应该会是首选的查询方式。它是一个高级 *全文查询* ，这表示它既能处理全文字段，又能处理精确字段。这就是说， `match` 查询主要的应用场景就是进行全文搜索。



例如用 `match` 搜索一个词

```http
GET /my_index/my_type/_search
{
    "query": {
        "match": {
            "title": "QUICK!"
        }
    }
}
```

他会查询title带有QUICK!的文档，并且不区分大小写

`match` 还可以查询多个词

```http
GET /my_index/my_type/_search
{
    "query": {
        "match": {
            "title": "BROWN DOG!"
        }
    }
}
```

他会查询title中带有BROWN和DOG!的任意一个的文档，并且不区分大小写



`match` 查询还可以接受 `operator` 操作符作为输入参数，默认情况下该操作符是 `or` 。我们可以将它修改成 `and` 

```http
GET /my_index/my_type/_search
{
    "query": {
        "match": {
            "title": {      
                "query":    "BROWN DOG!",
                "operator": "and"
            }
        }
    }
}
```

此时就要求查找到的文档同时包含BROWN和DOG!



`match` 查询支持 `minimum_should_match` 最小匹配参数，这让我们可以指定必须匹配的词项数用来表示一个文档是否相关

```http
GET /my_index/my_type/_search
{
  "query": {
    "match": {
      "title": {
        "query":                "quick brown dog",
        "minimum_should_match": "75%"
      }
    }
  }
}
```

当给定百分比的时候， `minimum_should_match` 会做合适的事情：在之前三词项的示例中， `75%` 会自动被截断成 `66.6%` ，即三个里面两个词。无论这个值设置成什么，至少包含一个词项的文档才会被认为是匹配的。



`match` 也可以和 `bool` 查询一起使用

```http
GET /my_index/my_type/_search
{
  "query": {
    "bool": {
      "must":     { "match": { "title": "quick" }},
      "must_not": { "match": { "title": "lazy"  }},
      "should": [
                  { "match": { "title": "brown" }},
                  { "match": { "title": "dog"   }}
      ]
    }
  }
}
```

 `minimum_should_match` 参数控制需要匹配的 `should` 语句的数量，它既可以是一个绝对的数字，又可以是个百分比

```http
GET /my_index/my_type/_search
{
  "query": {
    "bool": {
      "should": [
        { "match": { "title": "brown" }},
        { "match": { "title": "fox"   }},
        { "match": { "title": "dog"   }}
      ],
      "minimum_should_match": 2 
    }
  }
}
```



## 多字段搜索

`multi_match` 查询为能在多个字段上反复执行相同查询提供了一种便捷方式。

`multi_match` 多匹配查询的类型有三种：`best_fields` 、 `most_fields` 和 `cross_fields` （最佳字段、多数字段、跨字段）。默认情况下，查询的类型是 `best_fields` ，这表示它会为每个字段生成一个 `match` 查询



```json
{
    "multi_match": {
        "query":                "Quick brown fox",
        "type":                 "best_fields", 
        "fields":               [ "title", "body" ],
        "tie_breaker":          0.3,
        "slop":					30,
        "minimum_should_match": "30%" 
    }
}
```

* `query` 为查询的内容
* `type` 为查询的类型：`best_fields` 、 `most_fields` 和 `cross_fields` 
* `fields` 为查询的字段，也可以使用 `*` 来模糊匹配：`*_title`， 而是用 `^` 可以提升一个字段的查询权重：`title^2` 则title的权重为2
* `minimum_should_match ` 为匹配度
* `slop` 是查询临近度，也就是前一个单词和后一个单词的距离，分数越高越近，分数越低越远
* `tie_breaker` 为评分系数，评分会乘上这个值



## 近似匹配



### 短语匹配

如果你查询的是一个连续的短语，那么 `match` 似乎就不能了，这时需要使用 `match_phrase` 查询

```http
GET /my_index/my_type/_search
{
    "query": {
        "match_phrase": {
            "title": "quick brown fox"
        }
    }
}
```

相当于

```json
"match": {
    "title": {
        "query": "quick brown fox",
        "type":  "phrase"
    }
}
```

那么会把quick brown fox当成短语在文中搜索，不会断开，必须是全部连续





## 部分匹配

部分匹配类似于SQL中的

```sql
  WHERE text LIKE "%quick%"
      AND text LIKE "%brown%"
      AND text LIKE "%fox%"
```



### 前缀查询

```json
"prefix": {
    "field": "value"
}
```

例如

```http
GET /my_index/address/_search
{
    "query": {
        "prefix": {
            "postcode": "W1"
        }
    }
}
```

查询postcode字段以W1开头的文档



### 正则表达式匹配

`regexp` 使用正则表达式匹配，例如：

```http
GET /my_index/address/_search
{
    "query": {
        "wildcard": {
            "postcode": "W?F*HW" 
        }
    }
}
```



### 短语开头

 `match_phrase_prefix` 和 `match_phras` 差不多，只是规定了以这个短语开头

```json
{
    "match_phrase_prefix" : {
        "brand" : "johnnie walker bl"
    }
}
```



## 分页搜索

和 SQL 使用 `LIMIT` 关键字返回单个 `page` 结果的方法相同，Elasticsearch 接受 `from` 和 `size` 参数： 

- **`size`**

  一页的文档数量，默认是 `10`

- **`from`**

  从第几个文档开始返回，默认是 `0`

如果每页展示 5 条结果，可以用下面方式请求得到 1 到 3 页的结果：

```http
GET /_search?size=5
```

```http
GET /_search?size=5&from=5
```

```http
GET /_search?size=5&from=10
```

除了把 `size` 和 `from` 放在URI参数上还可以放在请求体中

```http
GET /{index}/{type}/_search

{
	"query": {
		...
	},
	"size": 10,
	"from": 20
}
```

