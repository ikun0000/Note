# Elasticsearch 聚合查询

Elasticsearch的聚合查询有两个概念：

* **Buckets**

  满足特定条件的文档集合

* **Metrics**

  对Buckets内的文档进行统计计算

类比于SQL中的聚合分组查询：

```sql
SELECT COUNT(color) 
FROM table
GROUP BY color 
```

`COUNT(color)` 相当于Metrics，`GROUP BY color` 相当于Buckets。

也就是Buckets在概念上类似于 SQL 的分组（GROUP BY），而Metrics则类似于 `COUNT()` 、 `SUM()` 、 `MAX()` 等统计方法。



## 单个聚合查询

聚合查询的格式如下

```json
{
    "aggs": {
        "查询后的字段名": {
            "聚合方式": {
                "field": "聚合的字段"
            }
        },
        ...
    }
}
```

**查询后的字段名**就是查询后返回的字段的命名，查询方式有：`terms`（统计这个字段的所有值在文档中分别出现的次数，也就是`SELECT count(f) FROM xxx GROUP BY f`）、`sum`（求和）、`avg`（平均值）、`min`（最小值）、`max`（最大值） 等。

例如

```http
GET /cars/transactions/_search HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "size" : 0,
    "aggs" : { 
        "most_expanse" : { 
            "terms" : { 
              "field" : "price"
            }
        }
    }
}
```

response

```json
{
    "took": 16,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 8,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    },
    "aggregations": {
        "most_expanse": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": 20000,
                    "doc_count": 2
                },
                {
                    "key": 10000,
                    "doc_count": 1
                },
                {
                    "key": 12000,
                    "doc_count": 1
                },
                {
                    "key": 15000,
                    "doc_count": 1
                },
                {
                    "key": 25000,
                    "doc_count": 1
                },
                {
                    "key": 30000,
                    "doc_count": 1
                },
                {
                    "key": 80000,
                    "doc_count": 1
                }
            ]
        }
    }
}
```

> 针对 `text` 类型使用聚合查询报错
>
> ```json
> {
>     "error": {
>         "root_cause": [
>             {
>                 "type": "illegal_argument_exception",
>                 "reason": "Text fields are not optimised for operations that require per-document field data like aggregations and sorting, so these operations are disabled by default. Please use a keyword field instead. Alternatively, set fielddata=true on [make] in order to load field data by uninverting the inverted index. Note that this can use significant memory."
>             }
>         ],
>         "type": "search_phase_execution_exception",
>         "reason": "all shards failed",
>         "phase": "query",
>         "grouped": true,
>         "failed_shards": [
>             {
>                 "shard": 0,
>                 "index": "cars",
>                 "node": "zmyt6JeuSvubm12QMM94jQ",
>                 "reason": {
>                     "type": "illegal_argument_exception",
>                     "reason": "Text fields are not optimised for operations that require per-document field data like aggregations and sorting, so these operations are disabled by default. Please use a keyword field instead. Alternatively, set fielddata=true on [make] in order to load field data by uninverting the inverted index. Note that this can use significant memory."
>                 }
>             }
>         ],
>         "caused_by": {
>             "type": "illegal_argument_exception",
>             "reason": "Text fields are not optimised for operations that require per-document field data like aggregations and sorting, so these operations are disabled by default. Please use a keyword field instead. Alternatively, set fielddata=true on [make] in order to load field data by uninverting the inverted index. Note that this can use significant memory.",
>             "caused_by": {
>                 "type": "illegal_argument_exception",
>                 "reason": "Text fields are not optimised for operations that require per-document field data like aggregations and sorting, so these operations are disabled by default. Please use a keyword field instead. Alternatively, set fielddata=true on [make] in order to load field data by uninverting the inverted index. Note that this can use significant memory."
>             }
>         }
>     },
>     "status": 400
> }
> ```
> 需要在创建索引类型的时候指定 `fielddata` 为 `true` ，***但是这样做会耗费大量的堆空间***



## 嵌套聚合查询

聚合查询可以嵌套使用

```json
{
    "aggs": {
        "查询后的字段名": {
            "聚合方式": {
                "field": "聚合的字段"
            },
            "aggs": {
                "嵌套aggs查询后字段名": {
                    "聚合方式": {
                        "field": "聚合的字段"
                    }
                }
            }
        },
        ...
    }
}
```

例如

```http
GET /cars/transactions/_search
{
   "size" : 0,
   "aggs": {
      "colors": {
         "terms": {
            "field": "color"
         },
         "aggs": { 
            "avg_price": { 
               "avg": {
                  "field": "price" 
               }
            }
         }
      }
   }
}
```

首先以color的值把文档分类，然后对不同color的文档集合分别对price求平均值，如果换成SQL如下

```sql
SELECT AVG(price) FROM cars.transactions GROUP BY color
```

