# ElasticSearch索引和文档相关



## 索引操作

### 添加索引

往 Elasticsearch 添加数据时需要用到 *索引* —— 保存相关数据的地方。 索引实际上是指向一个或者多个物理 *分片* 的 *逻辑命名空间*。添加索引的格式如下

```http
PUT /{index}
{
    "settings": { ... any settings ... },
    "mappings": {
        "type_one": {
        	"properties": {
        		"field name 1": {
        			"type": "type name",
        			"analyzer": "分析器",
        			"stopwords": ["停用词1", "停用词2", ...],
        			"format": "格式",
        			"fielddata": "true|false"
        		},
        		"field name 2": {
        			"type": "type name",
        			"analyzer": "语言分析器",
        			"stopwords": ["停用词1", "停用词2", ...],
        			"format": "格式",
        			"fielddata": "true|false"
        		},
        		...
        	}
        },
        "type_two": { ... any mappings ... },
        ...
    }
}
```

语言分析器可以让你在特定语言环境下搜索做些优化，比如英文会去掉一些复数，at，or之类的，还可以识别错别字，现在的话 `analyzer` 使用 `english`、`chinese` 、`french`、`german` 和 `spanish`应该足够了。`stopwords` 用于标记搜索时停用那些词，因为有些词是起语气词，这些词对搜索没有用，所以屏蔽掉以提高精度。如果这个字段是`text` 类型的话 `fielddata` 设置为true之后，那么就可以被聚合计算，否则会报错。

> 这对中文有一个ik分词器，需要自己安装，然后就可以在`analyzer` 中使用ik分词器了
>
> [elasticsearch-analysis-ik]( https://github.com/medcl/elasticsearch-analysis-ik )

例如

```shell
curl --location --request PUT '10.10.10.246:9200/people' \
--header 'Content-Type: application/json' \
--data-raw '{
	"settings": {
		"number_of_shards": 3,
		"number_of_replicas": 0
	},
	"mappings": {
		"man": {
			"properties": {
				"name": {
					"type": "text"
				},
				"country": {
					"type": "keyword"
				},
				"age": {
					"type": "integer"
				},
				"date": {
					"type": "date",
					"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
				}
			}
		}
	}
}'
```

这是创建一个people索引并且创建一个man的类型并且设置了字段和类型。



在包含一个空节点的集群内创建名为 `blogs` 的索引，索引在默认情况下会被分配5个主分片， 但是为了演示目的，我们将分配3个主分片和一份副本（每个主分片拥有一个副本分片）：

```http
PUT /blogs HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
    "settings": {
        "number_of_shards": 3,
        "number_of_replicas": 1
    }
}
```
创建出来的索引：
![](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0202.png)

### 删除索引

删除索引的语法如下

删除一个索引

```http
DELETE /{index}
```

删除多个索引

```http
DELETE /index_one,index_two
```

```http
DELETE /index_*
```

或者这样删除所有索引

```http
DELETE /_all
```

```http
DELETE /*
```



## 文档

在大多数应用中，多数实体或对象可以被序列化为包含键值对的 JSON 对象。 一个 *键* 可以是一个字段或字段的名称，一个 *值* 可以是一个字符串，一个数字，一个布尔值， 另一个对象，一些数组值，或一些其它特殊类型诸如表示日期的字符串，或代表一个地理位置的对象。通常情况下，我们使用的术语 *对象* 和 *文档* 是可以互相替换的，不过，有一个区别： 一个对象仅仅是类似于 hash 、 hashmap 、字典或者关联数组的 JSON 对象，对象中也可以嵌套其他的对象。 对象可能包含了另外一些对象。在 Elasticsearch 中，术语 *文档* 有着特定的含义。它是指最顶层或者根对象, 这个根对象被序列化成 JSON 并存储到 Elasticsearch 中，指定了唯一 ID。



### 文档元数据

一个文档不仅仅包含它的数据 ，也包含 *元数据* —— *有关* 文档的信息。 三个必须的元数据元素如下：

* `_index`

  文档在哪存放

  一个 *索引* 应该是因共同的特性被分组到一起的文档集合。 例如，你可能存储所有的产品在索引 `products` 中，而存储所有销售的交易到索引 `sales` 中。 虽然也允许存储不相关的数据到一个索引中，但这通常看作是一个反模式的做法。

* `_type`

  文档表示的对象类别

  数据可能在索引中只是松散的组合在一起。Elasticsearch 公开了一个称为 *types* （类型）的特性，它允许在索引中对数据进行逻辑分区。不同 types 的文档可能有不同的字段，但最好能够非常相似。一个 `_type` 命名可以是大写或者小写，但是不能以下划线或者句号开头，不应该包含逗号， 并且长度限制为256个字符。

* `_id`

  文档唯一标识

  *ID* 是一个字符串，当它和 `_index` 以及 `_type` 组合就可以唯一确定 Elasticsearch 中的一个文档。 当你创建一个新的文档，要么提供自己的 `_id` ，要么让 Elasticsearch 帮你生成。



### 文档操作



#### 添加文档

索引/添加文档的格式如下

```http
PUT /{index}/{type}/{id}

{
  "field": "value",
  ...
}
```

这种格式需要自己给定不重复的ID，比如向索引为website类型为blog插入id为123的数据

```http
PUT /website/blog/123 HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
  "title": "My first blog entry",
  "text":  "Just trying this out...",
  "date":  "2014/01/01"
}
```

response

```json
{
    "_index": "website",
    "_type": "blog",
    "_id": "123",
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



如果不想自己指定ID也可以让ES自动生成。只需要把请求方法换成POST并且在添加文档时不提供ID那么就会自动生成ID并返回

```http
POST /website/blog HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
  "title": "My second blog entry",
  "text":  "Still trying this out...",
  "date":  "2014/01/01"
}
```

response

```json
{
    "_index": "website",
    "_type": "blog",
    "_id": "82zxxnMBmvSkMRE8jGqQ",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 1,
    "_primary_term": 1
}
```

自动生成的 ID 是 URL-safe、 基于 Base64 编码且长度为20个字符的 GUID 字符串。 这些 GUID 字符串由可修改的 FlakeID 模式生成，这种模式允许多个节点并行生成唯一 ID ，且互相之间的冲突概率几乎为零。



#### 检查文档是否存在

如果只想检查一个文档是否存在根本不想关心内容，那么用 `HEAD` 方法来代替 `GET` 方法。 `HEAD` 请求没有返回体，只返回一个 HTTP 请求报头：

```http
HEAD /website/blog/123 HTTP/1.1
Host: 10.10.10.246:9200
```

如果存在会返回状态码200，找不到会返回i404



#### 获取文档

获取文档的格式如下

```http
GET /{index}/{type}/{id}
```

例如

```http
GET /website/blog/123 HTTP/1.1
Host: 10.10.10.246:9200
```

response

```json
{
  "_index": "website",
  "_type": "blog",
  "_id": "123",
  "_version": 1,
  "_seq_no": 0,
  "_primary_term": 1,
  "found": true,
  "_source": {
    "title": "My first blog entry",
    "text": "Just trying this out...",
    "date": "2014/01/01"
  }
}
```

如果文档不存在就会返回404



默认情况下会返回i整个文档，如果只是获取某个字段可以用以下方式获取

```http
GET /{index}/{type}/{id}?_source=[field1],[field2]...
```

例如

```http
GET /website/blog/123?_source=title,date HTTP/1.1
Host: 10.10.10.246:9200
```

response

```json
{
    "_index": "website",
    "_type": "blog",
    "_id": "123",
    "_version": 1,
    "_seq_no": 0,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "date": "2014/01/01",
        "title": "My first blog entry"
    }
}
```



### 更新文档

在 Elasticsearch 中文档是 *不可改变* 的，不能修改它们。相反，如果想要更新现有的文档，需要 *重建索引* 或者进行替换。方法是使用使用相同的请求去替换

先添加文档：

```http
PUT /{index}/{type}/{id}

{
  "field": "first",
  ...
}
```

然后使用相同的请求方法和URI，只是请求体不同：

```http
PUT /{index}/{type}/{id}

{
  "field": "new",
  ...
}
```

例如

```http
PUT /website/blog/123 HTTP/1.1
Host: 10.10.10.246:9200
Content-Type: application/json

{
  "title": "My first blog entry",
  "text":  "I am starting to get the hang of this...",
  "date":  "2014/01/02"
}
```

response

```json
{
    "_index": "website",
    "_type": "blog",
    "_id": "123",
    "_version": 2,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 2,
    "_primary_term": 1
}
```

`result` 字段也不是 `created`而是 `updated` 



### 删除文档

删除文档的语法和我们所知道的规则相同，只是使用 `DELETE` 方法

```http
DELETE /{index}/{type}/{id}
```

例如

```http
DELETE /website/blog/123 HTTP/1.1
Host: 10.10.10.246:9200
```

response

```json
{
    "_index": "website",
    "_type": "blog",
    "_id": "123",
    "_version": 3,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 3,
    "_primary_term": 1
}
```

同时 `_version` 也自增了，如果找不到要删除的文档就会返回404