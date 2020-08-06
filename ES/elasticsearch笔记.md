# ElasticSearch笔记



## ElasticSearch简介

Elasticsearch 是一个分布式、可扩展、实时的搜索与数据分析引擎。 它能从项目一开始就赋予你的数据以搜索、分析和探索的能力，这是通常没有预料到的。 它存在还因为原始数据如果只是躺在磁盘里面根本就毫无用处。

他提供了全文索引和结构化数据实时统计，当然，他还支持复杂的人类语言处理，地理位置和对象间关联关系等。



## 安装

安装和可视化控制台去看另外一篇笔记



测试是否安装成功

```shell
curl --location --request GET 'http://10.10.10.246:9200/?pretty'
```

如果返回以下类似的内容就是安装成功

```json
{
  "name": "8f71d9eaed3b",
  "cluster_name": "docker-cluster",
  "cluster_uuid": "7uIt_VW0RrWBUsfIheyUxw",
  "version": {
    "number": "7.8.1",
    "build_flavor": "default",
    "build_type": "docker",
    "build_hash": "b5ca9c58fb664ca8bf9e4057fc229b3396bf3a89",
    "build_date": "2020-07-21T16:40:44.668009Z",
    "build_snapshot": false,
    "lucene_version": "8.5.1",
    "minimum_wire_compatibility_version": "6.8.0",
    "minimum_index_compatibility_version": "6.0.0-beta1"
  },
  "tagline": "You Know, for Search"
}
```

这是一个es节点的信息。**节点**就是一个运行的ES实例，**集群**是拥有相同的 ` cluster.name ` 的节点，他们能一起工作并共享数据，还提供容错与可伸缩性，不过一个节点也是一个集群。如过需要修改节点名称就在ES目录中的配置文件 ` elasticsearch.yml ` 中修改 ` cluster.name ` 的值



## 交互

和ES集群打交道主要有两种方法，Java API和RESTful API



### Java API

如果使用java的话可以使用两个内置的客户端

* **节点客户端（Node client）**：使用节点客户端会以非数据节点的形式加入到本地集群中。也就是说，这个节点客户端不保存任何数据，但他知道需要的数据在哪个节点上并把请求转发给那个节点。
* **传输客户端（Transport client）**：传输客户端可以把请求发送到远程集群或是集群中的一个节点上，但自己并不在集群中。

使用这两个客户端通过9300端口访问，集群中的节点也是通过9300端口进行交互



### RESTful API

所有语言的客户端都可以使用RESTful API访问9200端口操作es，也就是说可以使用任何能发送http请求的东西来做客户端。



举个栗子，使用curl发送请求，这条请求查看集群中文档的数量

```shell
curl --location --request GET 'http://10.10.10.246:9200/_count?pretty' \
--header 'Content-Type: application/json' \
--data-raw '{
    "query": {
        "match_all": {}
    }
}'
```

对应的HTTP包是，为了好看使用HTTP包做展示了

```http
GET /_count?pretty HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "match_all": {}
    }
}
```

返回的是JSON数据

```json
{
  "count": 0,
  "_shards": {
    "total": 0,
    "successful": 0,
    "skipped": 0,
    "failed": 0
  }
}
```



## 术语意思

查看另一篇笔记



向集群中添加一个文档，或者说一行数据：

```http
PUT /megacorp/employee/1 HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "first_name": "John",
    "last_name": "Smith",
    "age": 25,
    "about": "I love to go rock climbing",
    "interests": ["sports", "music"]
}
```

返回

```json
{
    "_index": "megacorp",
    "_type": "employee",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```

请求的URI有三部分组成： `/<索引名>/<类型>/<数据id>` 

上面例子中的索引是megacorp，类型是employee，id为1，body中的就是要存储的数据

> 使用PUT请求需要自己提供ID，如果要ES自动生成那么就使用POST方式并且不提供ID
>
> ```http
> POST /megacorp/employee HTTP/1.1
> Host: 10.10.10.246:9200
> Content-Type: application/json
> 
> {
>     "first_name": "John",
>     "last_name": "Smith",
>     "age": 25,
>     "about": "I love to go rock climbing",
>     "interests": ["sports", "music"]
> }
> ```



## 轻量查询和使用查询表达式

通过ID检索一个文档

```http
GET /megacorp/employee/1 HTTP/1.1
Host: 10.10.10.246:9200
```

response

```json
{
    "_index": "megacorp",
    "_type": "employee",
    "_id": "1",
    "_version": 1,
    "_seq_no": 0,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "first_name": "John",
        "last_name": "Smith",
        "age": 25,
        "about": "I love to go rock climbing",
        "interests": [
            "sports",
            "music"
        ]
    }
}
```



> 将 HTTP 命令由 `PUT` 改为 `GET` 可以用来检索文档，同样的，可以使用 `DELETE` 命令来删除文档，以及使用 `HEAD` 指令来检查文档是否存在。如果想更新已存在的文档，只需再次 `PUT` 



前面是指定一个ID索索一个文档，下面是索索全部文档

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
```

response

```json
{
    "took": 49,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 3,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 1.0,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                }
            },
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "2",
                "_score": 1.0,
                "_source": {
                    "first_name": "Jane",
                    "last_name": "Smith",
                    "age": 32,
                    "about": "I like to collect rock albums",
                    "interests": [
                        "music"
                    ]
                }
            },
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "3",
                "_score": 1.0,
                "_source": {
                    "first_name": "Douglas",
                    "last_name": "Fir",
                    "age": 35,
                    "about": "I like to build cabinets",
                    "interests": [
                        "forestry"
                    ]
                }
            }
        ]
    }
}
```



然后使用条件搜索，找到与搜索字段匹配的文档，在这个例子就是找到 `last_name` 是Smith的文档

```http
GET /megacorp/employee/_search?q=last_name:Smith HTTP/1.1
Host: 10.10.10.246:9200
```

response

```json
{
    "took": 2,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 2,
            "relation": "eq"
        },
        "max_score": 0.4700036,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 0.4700036,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                }
            },
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "2",
                "_score": 0.4700036,
                "_source": {
                    "first_name": "Jane",
                    "last_name": "Smith",
                    "age": 32,
                    "about": "I like to collect rock albums",
                    "interests": [
                        "music"
                    ]
                }
            }
        ]
    }
}
```



> `_search` 是查询端点，`q` 参数是查询条件。但是使用 `q` 来查询有诸多不便，ES提供了一个丰富灵活的查询语言叫做 *查询表达式* ， 它支持构建更加复杂和健壮的查询。

以下两个查询的效果相同

```http
GET /megacorp/employee/_search?q=last_name:Smith HTTP/1.1
Host: 10.10.10.246:9200
```

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "match": {
            "last_name": "Smith"
        }
    }
}
```

查询所有换成JSON形式的查询表达式如下

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "match_all": {

        }
    }
}
```



一个范围的查询，查询 `last_name` 是Smith和 `age` 大于30的文档 ，这里使用 `filter` 为查询添加一个 `range` 过滤器 

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "bool": {
            "must": {
                "match": {
                    "last_name": "Smith"
                }
            },
            "filter": {
                "range": {
                    "age": {
                        "gt": 30
                    }
                }
            }
        }
    }
}
```

response

```json
{
    "took": 89,
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
        "max_score": 0.4700036,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "2",
                "_score": 0.4700036,
                "_source": {
                    "first_name": "Jane",
                    "last_name": "Smith",
                    "age": 32,
                    "about": "I like to collect rock albums",
                    "interests": [
                        "music"
                    ]
                }
            }
        ]
    }
}
```



## 全文搜索

全文搜索对于传统的关系型数据库是一个很耗费资源和时间的任务



查询指定字段包含特定内容的文档，这个和上面查询 `last_name` 一样都是使用 `match` 查询

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "match": {
            "about": "rock climbing"
        }
    }
}
```

response

```json
{
    "took": 27,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 2,
            "relation": "eq"
        },
        "max_score": 1.4167401,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 1.4167401,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                }
            },
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "2",
                "_score": 0.4589591,
                "_source": {
                    "first_name": "Jane",
                    "last_name": "Smith",
                    "age": 32,
                    "about": "I like to collect rock albums",
                    "interests": [
                        "music"
                    ]
                }
            }
        ]
    }
}
```

ES默认按照相关性得分排序，即每个文档跟查询的匹配程度。第一个最高得分的结果很明显：John Smith 的 `about` 属性清楚地写着 “rock climbing”。

但为什么 Jane Smith 也作为结果返回了呢？原因是她的 `about` 属性里提到了 “rock” 。因为只有 “rock” 而没有 “climbing” ，所以她的相关性得分低于 John 的。

这是一个很好的案例，阐明了 Elasticsearch 如何 *在* 全文属性上搜索并返回相关性最强的结果。Elasticsearch中的 *相关性* 概念非常重要，也是完全区别于传统关系型数据库的一个概念，数据库中的一条记录要么匹配要么不匹配。



## 短语搜索

有时候不想查询时出现以上示例中的搜索短语是rock climbing，但是返回的记录即有包含这个短语的也有只包含这个短语一部分的文档，也就是，我们希望能够完全匹配短语。比如， 我们想执行这样一个查询，仅匹配同时包含 “rock” *和* “climbing” ，*并且* 二者以短语 “rock climbing” 的形式紧挨着的记录。这是我们不能使用 `match` 查询而应该使用 `match_phrase` 查询。

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "match_phrase": {
            "about": "rock climbing"
        }
    }
}
```



## 匹配到的文本高亮显示

有时会在ES中放置一些文章，然后web应用提供关键字搜索，用用户输入的关键字去ES中查询符合的文章并把对应的关键字使用 `<em></em>`标签高亮显示，那么需要在 `query` 同级之下添加一个 `highlight`

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "query": {
        "match_phrase": {
            "about": "rock climbing"
        }
    },
    "highlight": {
        "fields": {
            "about": {}
        }
    }
}
```

response

```json
{
    "took": 95,
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
        "max_score": 1.4167401,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 1.4167401,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                },
                "highlight": {
                    "about": [
                        "I love to go <em>rock</em> <em>climbing</em>"
                    ]
                }
            }
        ]
    }
}
```



## 聚合查询

ES有一个功能叫聚合（aggregations），允许我们基于数据生成一些精细的分析结果。聚合与 SQL 中的 `GROUP BY` 类似但更强大，他的语法为

```json
{
    "aggs": {
        "结果名称": {
            "聚合的类型": {
                "field": "聚合的字段"
            }
        }
    }
}
```

这是一个例子

```http
GET /megacorp/employee/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "aggs": {
        "all_interests": {
            "terms": {
                "field": "age"
            }
        }
    }
}
```

response

```json
{
	...
    "aggregations": {
        "all_interests": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": 25,
                    "doc_count": 1
                },
                {
                    "key": 32,
                    "doc_count": 1
                },
                {
                    "key": 35,
                    "doc_count": 1
                }
            ]
        }
    }
}
```

> 如果带上 `query` 查询条件的话那么会先做条件查询然后聚合。
>
> 同时聚合也支持套娃模式，在一个聚合之后再用聚合的内容在做一次聚合查询，语法如下：
>
> ```json
> {
>     "aggs" : {
>         "返回结果名1" : {
>             "聚合方式1" : { "field" : "聚合字段1" },
>             "aggs" : {
>                 "返回结果名2" : {
>                     "聚合方式2" : { "field" : "聚合字段2" }
>                 }
>             }
>         }
>     }
> }
> ```



## 跨越索引和类型搜索

**`/_search`**

在所有的索引中搜索所有的类型

**`/gb/_search`**

在 `gb` 索引中搜索所有的类型

**`/gb,us/_search`**

在 `gb` 和 `us` 索引中搜索所有的文档

**`/g\*,u\*/_search`**

在任何以 `g` 或者 `u` 开头的索引中搜索所有的类型

**`/gb/user/_search`**

在 `gb` 索引中搜索 `user` 类型

**`/gb,us/user,tweet/_search`**

在 `gb` 和 `us` 索引中搜索 `user` 和 `tweet` 类型

**`/_all/user,tweet/_search`**

在所有的索引中搜索 `user` 和 `tweet` 类型



## 查询分页

Elasticsearch 接受 `from` 和 `size` 参数 ：

**`size`**

显示应该返回的结果数量，默认是 `10`

**`from`**

显示应该跳过的初始结果数量，默认是 `0`

```http
GET /_search?size=5&from=10 HTTP/1.1
Host: 10.10.10.246:9200
```

或者

```http
GET /_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "size": 5,
    "from": 10
}
```



## 查询表达式

要使用这种查询表达式，只需将查询语句传递给 `query` 参数：

```http
GET /_search
{
    "query": YOUR_QUERY_HERE
}
```



### 查询结构

一个查询语句的典型结构：

```json
{
    "查询名字": {
        "查询字段1": "VALUE",
        "查询字段2": "VALUE",
        ...
    }
}
```

如果是针对某个字段，那么它的结构如下：

```json
{
    "查询名字": {
        "过滤器名": {
            "查询字段1": "VALUE",
        	"查询字段2": "VALUE",
            ...
        }
    }
}
```



### 合并查询语句

一个 `bool` 语句 允许在你需要的时候组合其它语句，无论是 `must` 匹配、 `must_not` 匹配还是 `should` 匹配，同时它可以包含不评分的过滤器（filters）：

```json
{
    "bool": {
        "must":     { "match": { "tweet": "elasticsearch" }},
        "must_not": { "match": { "name":  "mary" }},
        "should":   { "match": { "tweet": "full text" }},
        "filter":   { "range": { "age" : { "gt" : 30 }} }
    }
}
```



### 查询方式

* **`match_all` 查询**

  `match_all` 查询简单的匹配所有文档。在没有指定查询方式时，它是默认的查询： 

  ```json
  { "match_all": {}}
  ```

* **`match` 查询**

  无论你在任何字段上进行的是全文搜索还是精确查询，`match` 查询是你可用的标准查询。如果你在一个全文字段上使用 `match` 查询，在执行查询前，它将用正确的分析器去分析查询字符串：

  ```json
  { "match": { "tweet": "About Search" }}
  ```

  如果在一个精确值的字段上使用它，例如数字、日期、布尔或者一个 `not_analyzed` 字符串字段，那么它将会精确匹配给定的值：

  ```json
  { "match": { "age":    26           }}
  { "match": { "date":   "2014-09-01" }}
  { "match": { "public": true         }}
  { "match": { "tag":    "full_text"  }}
  ```

* **`multi_match` 查询**

   `multi_match` 查询可以在多个字段上执行相同的 `match` 查询：

  ```json
  {
      "multi_match": {
          "query":    "full text search",
          "fields":   [ "title", "body" ]
      }
  }
  ```

* **`range` 查询**

   `range` 查询找出那些落在指定区间内的数字或者时间： 

  ```json
  {
      "range": {
          "age": {
              "gte":  20,
              "lt":   30
          }
      }
  }
  ```

  被允许的操作符如下：

  - **`gt`**

    大于

  - **`gte`**

    大于等于

  - **`lt`**

    小于

  - **`lte`**

    小于等于

* **`term` 查询**

   `term` 查询被用于精确值匹配，这些精确值可能是数字、时间、布尔或者那些 `not_analyzed` 的字符串：

  ```json
  { "term": { "age":    26           }}
  { "term": { "date":   "2014-09-01" }}
  { "term": { "public": true         }}
  { "term": { "tag":    "full_text"  }}
  ```

* **`terms` 查询**

   `terms` 查询和 `term` 查询一样，但它允许你指定多值进行匹配。如果这个字段包含了指定值中的任何一个值，那么这个文档满足条件：

  ```json
  { "terms": { "tag": [ "search", "full_text", "nosql" ] }}
  ```

* **`exists` 和 `missing` 查询**

   `exists` 查询和 `missing` 查询被用于查找那些指定字段中有值 (`exists`) 或无值 (`missing`) 的文档。这与SQL中的 `IS_NULL` (`missing`) 和 `NOT IS_NULL` (`exists`) 在本质上具有共性：

  ```json
  {
      "exists":   {
          "field":    "title"
      }
  }
  ```



### 组合多查询

**`must`**

文档 *必须* 匹配这些条件才能被包含进来。

**`must_not`**

文档 *必须不* 匹配这些条件才能被包含进来。

**`should`**

如果满足这些语句中的任意语句，将增加 `_score` ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。

**`filter`**

*必须* 匹配，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档。

```json
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }},
            { "range": { "date": { "gte": "2014-01-01" }}}
        ]
    }
}
```



## 排序

排序语法

```json
{
	...,
    "sort": {
    	"排序的字段1": {
    		"order": "<asc|desc>"
		},
		"排序的字段2": {
    		"order": "<asc|desc>"
		},
	}
}
```

一下是对一个字段排序和对多个字段排序

```http
GET /_search
{
    "query" : {
        "bool" : {
            "filter" : { "term" : { "user_id" : 1 }}
        }
    },
    "sort": { "date": { "order": "desc" }}
}
```

```http
GET /_search
{
    "query" : {
        "bool" : {
            "must":   { "match": { "tweet": "manage text search" }},
            "filter" : { "term" : { "user_id" : 2 }}
        }
    },
    "sort": [
        { "date":   { "order": "desc" }},
        { "_score": { "order": "desc" }}
    ]
}
```

