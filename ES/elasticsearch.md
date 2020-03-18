# ES基础

## ElasticSearch安装

```shell
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:tag
```

访问http://<ip_addr>:9200，出现下面的json表示安装成功

```json
{
  "name" : "R1gBQDD",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "pfYjQ7IpT3ChZ5N2iH_KJw",
  "version" : {
    "number" : "6.8.7",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "c63e621",
    "build_date" : "2020-02-26T14:38:01.193138Z",
    "build_snapshot" : false,
    "lucene_version" : "7.7.2",
    "minimum_wire_compatibility_version" : "5.6.0",
    "minimum_index_compatibility_version" : "5.0.0"
  },
  "tagline" : "You Know, for Search"
}
```

在本地安装运行elasticsearch-head

```shell
git clone git://github.com/mobz/elasticsearch-head.git
cd elasticsearch-head
npm install
npm run start
```

访问http://localhost:9100/ ，在顶部输入框输入es的地址连接，会连接不上，这是es的跨域问题

进入es的容器修改~/config/elasticsearch.yml，添加两行配置：

```yaml
http.cors.enabled: true
http.cors.allow-origin: "*"
```



## ES集群配置

master节点elasticsearch.yml配置：

```yaml
http.cors.enabled: true
http.cors.allow-origin: "*"

cluster.name: group1		# 集群名字
node.name: master			# 节点名字
node.master: true			# 指定这个节点是否是主节点

network.host: 127.0.0.1		# 绑定本机ip
```

slave1节点elasticsearch.yml配置：

```yaml
cluster.name: group1		# 集群名字
node.name: slave1			# 节点名字

network.host: 127.0.0.1		# 绑定本机ip
http.port: 9400				# 改变监听端口

discovery.zen.ping.unicast.hosts: ["127.0.0.1:9200"]		# 绑定集群的主节点
```

slave2节点elasticsearch.yml配置：

```yaml
cluster.name: group1		# 集群名字
node.name: slave2			# 节点名字

network.host: 127.0.0.1		# 绑定本机ip
http.port: 9600				# 改变监听端口

discovery.zen.ping.unicast.hosts: ["127.0.0.1:9200"]		# 绑定集群的主节点
```

然后先启动主节点在启动从节点就可以了



## ES基础概念

* **索引**：含有相同属性的文档集合
* **类型**：索引可以定义一个或多个类型，文档必须属于一个类型
* **文档**：文档是可以被索引的基本数据单位

和RDBMS的关系

| ES    | RDBMS    |
| ----- | -------- |
| index | database |
| type | table |
| document | row |



* **分片**：每个索引都有多个分片，每个分片是一个Lucene索引
* **备份**：拷贝一份分片就完成了分片的备份



#### 常用数据类型

| type | description                                                  |
| ---- | ------------------------------------------------------------ |
| text | 用于全文索引的类型，text类型的字段不能用于排序, 也很少用于聚合。会分词，然后进行索引，支持模糊、精确查询，不支持聚合 |
| keyword | 用于关键词索引，支持过滤，排序，聚合操作。不进行分词，直接索引，支持模糊、精确查询，支持聚合 |
| byte | 有符号的8位整数, 范围: [-128 ~ 127] |
| short | 有符号的16位整数, 范围: [-32768 ~ 32767]|
| integer| 有符号的64位整数, 范围: [−2^63 ~ 2^63-1] |
| float | 32位单精度浮点数 |
| double | 64位双精度浮点数 |
| date | 存储日期，使用`format`指定日期格式，`||`分割多种日期格式 |
| boolean | 真值：true，"true"，"on"，"yes"，"1"，1           假值：false, "false", "off", "no", "0", ""(空字符串), 0.0, 0 |
| binary | 二进制类型是Base64编码字符串的二进制值，不以默认的方式存储, 且不能被搜索 |
| array | 数组类型，用`[]`括起来 |
| object | 对象类型，用`{}`括起来的键值对 |
| geo_point | 位置信息，存储经纬度，格式为：`{"lat": 33.11, "lon": 113.2}`，`"33.11, 113.2"`，`[ 33.11, 113.2 ]`，第一个（33.11）是经度，第二个（113.2）是维度 |
| ip | IP地址，`192.168.11.22`，`192.168.11.22/24` |



## ES CRUD

### ES创建索引

创建一个索引（相当于关系型数据库的数据库）`people`，设置了分片数量为3，备份数量为0。并且在这个索引下面创建一个类型（相当于关系型数据库的数据表），包含四个字段`name`，`country`，`age`，`date`（日期数据类型要设置格式，用`||`分开设置多种格式，`epoch_millis`为时间戳类型）

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

正确返回

```json
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "people"
}
```



### ES插入数据

在索引为`people`类型为`man`插入ID为1的数据，下面的例子是自己指定ID的

```shell
curl --location --request PUT '10.10.10.246:9200/people/man/1' \
--header 'Content-Type: application/json' \
--data-raw '{
	"name": "aaa",
	"country": "china",
	"age": 30,
	"date": "2001-05-23"
}'
```

正确返回

```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 1,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```



自动生成ID插入，注意请求方法改成`POST`

```shell
curl --location --request POST '10.10.10.246:9200/people/man' \
--header 'Content-Type: application/json' \
--data-raw '{
	"name": "bbb",
	"country": "china",
	"age": 40,
	"date": "9102-05-23"
}'
```

正确返回

```json
{
    "_index": "people",
    "_type": "man",
    "_id": "eh7V5nABZ9Qw6KRQEOxx",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 1,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```



### ES修改数据

#### 直接通过文档ID修改

修改上面插入的索引为`people`，类型为`man`，文档ID为1的数据

```shell
curl --location --request POST '10.10.10.246:9200/people/man/1/_update' \
--header 'Content-Type: application/json' \
--data-raw '{
	"doc": {
		"name": "aaa1"
	}
}'
```

正确返回

```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 2,
    "result": "updated",
    "_shards": {
        "total": 1,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 1,
    "_primary_term": 1
}
```



#### 通过脚本的方式修改

脚本语言可以使用内置的*paintless*，或者*python*，*javascript*，以下例子使用内置的*paintless*把文档ID为1的`age`加10

```shell
curl --location --request POST '10.10.10.246:9200/people/man/1/_update' \
--header 'Content-Type: application/json' \
--data-raw '{
	"script": {
		"lang": "painless",
		"inline": "ctx._source.age += 10"
	}
}'
```

正确返回

```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 3,
    "result": "updated",
    "_shards": {
        "total": 1,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 2,
    "_primary_term": 1
}
```

或者可以在外面指定参数

```shell
curl --location --request POST '10.10.10.246:9200/people/man/1/_update' \
--header 'Content-Type: application/json' \
--data-raw '{
	"script": {
		"lang": "painless",
		"inline": "ctx._source.age = params.age",
		"params": {
			"age": 100
		}
	}
}'
```



### ES删除数据

#### 删除文档

删除索引为`people`，类型为`man`，文档ID为eh7V5nABZ9Qw6KRQEOxx的文档

```shell
curl --location --request DELETE '10.10.10.246:9200/people/man/eh7V5nABZ9Qw6KRQEOxx' \
--header 'Content-Type: application/json'
```



#### 删除索引

1. 可以通过elasticsearch-head插件删除

2. http方法删除

   删除索引名为`book`的索引

   ```shell
   curl --location --request DELETE '10.10.10.246:9200/book' \
   --header 'Content-Type: application/json'
   ```

   

### ES查询数据

> 准备
>
> 1. 创建一个book索引
>
>    ```shell
>    curl --location --request PUT '10.10.10.246:9200/book' \
>    --header 'Content-Type: application/json' \
>    --data-raw '{
>    	"settings": {
>    		"number_of_shards": 5,
>    		"number_of_replicas": 0
>    	},
>    	"mappings": {
>    		"noval": {
>    			"properties": {
>    				"word_count": {
>    					"type": "integer"
>    				},
>    				"author": {
>    					"type": "keyword"
>    				},
>    				"title": {
>    					"type": "text"
>    				},
>    				"publish_date": {
>    					"format": "yyyy-MM-dd HH:MM:ss||yyyy-MM-dd||epoch_millis",
>    					"type": "date"
>    				}
>    			}
>    		}
>    	}
>    }'
>    ```
>
>    然后插入测试数据，略



查询索引为`book`，类型为`novel`，文档ID为gB4f53ABZ9Qw6KRQJ的文档：

```shell
curl --location --request GET '10.10.10.246:9200/book/noval/gB4f53ABZ9Qw6KRQJ-xn'
```

返回结果

```json
{
	"_index": "book",
	"_type": "noval",
	"_id": "gB4f53ABZ9Qw6KRQJ-xn",
	"_version": 1,
	"_seq_no": 0,
	"_primary_term": 1,
	"found": true,
	"_source": {
		"word_count": 90000,
		"author": "bbb",
		"title": "b1",
		"publish_date": "2001-1-2"
	}
}
```



#### 条件查询

查询所有数据

```shell
curl --location --request POST '10.10.10.246:9200/book/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"match_all": {}
	}
}'
```

返回值：

```json
{
	"took": 7,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"skipped": 0,
		"failed": 0
	},
	"hits": {
		"total": 6,
		"max_score": 1.0,
		"hits": [
			{
				"_index": "book",
				"_type": "noval",
				"_id": "gB4f53ABZ9Qw6KRQJ-xn",
				"_score": 1.0,
				"_source": {
					"word_count": 90000,
					"author": "bbb",
					"title": "b1",
					"publish_date": "2001-1-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "gR4f53ABZ9Qw6KRQWOzU",
				"_score": 1.0,
				"_source": {
					"word_count": 98000,
					"author": "cccc",
					"title": "c2",
					"publish_date": "2001-2-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "gh4f53ABZ9Qw6KRQ3ewq",
				"_score": 1.0,
				"_source": {
					"word_count": 98200,
					"author": "dddd",
					"title": "d4",
					"publish_date": "2002-2-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "hB4g53ABZ9Qw6KRQU-wa",
				"_score": 1.0,
				"_source": {
					"word_count": 9800,
					"author": "ffff",
					"title": "ff2",
					"publish_date": "2001-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "fx4e53ABZ9Qw6KRQ9ewu",
				"_score": 1.0,
				"_source": {
					"word_count": 100000,
					"author": "aaa",
					"title": "a1",
					"publish_date": "2001-1-1"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "gx4g53ABZ9Qw6KRQE-ya",
				"_score": 1.0,
				"_source": {
					"word_count": 98400,
					"author": "eeeee",
					"title": "e45",
					"publish_date": "1999-2-2"
				}
			}
		]
	}
}
```



分页查询，`size`指定一页有多少条数据，`from`指定返回哪一页

```shell
curl --location --request POST '10.10.10.246:9200/book/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"match_all": {}
	},
	"from": 1,
	"size": 1
}'
```

返回结果

```json
{
	"took": 8,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"skipped": 0,
		"failed": 0
	},
	"hits": {
		"total": 6,
		"max_score": 1.0,
		"hits": [
			{
				"_index": "book",
				"_type": "noval",
				"_id": "gR4f53ABZ9Qw6KRQWOzU",
				"_score": 1.0,
				"_source": {
					"word_count": 98000,
					"author": "cccc",
					"title": "c2",
					"publish_date": "2001-2-2"
				}
			}
		]
	}
}
```



查询含有`title`的数据

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"match": {
			"title": "测试"
		}
	}
}'
```

返回结果

```json
{
	"took": 14,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"skipped": 0,
		"failed": 0
	},
	"hits": {
		"total": 1,
		"max_score": 0.5753642,
		"hits": [
			{
				"_index": "book",
				"_type": "noval",
				"_id": "ih4353ABZ9Qw6KRQ_eyB",
				"_score": 0.5753642,
				"_source": {
					"word_count": 91100,
					"author": "测试作者",
					"title": "测试书籍",
					"publish_date": "2005-3-2"
				}
			}
		]
	}
}
```

> **注意点**
>
> * 查询时一个个字来查询的
> * 被查询的字段的类型要是text
> * 这个例子默认使用了分词，所以查询英文会查不到，创建mapping时*text*可以通过`index: false`来决定是否要分词，或者使用*keyword*类型



排序

按照`publish_date`降序

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"match_all": {}
	},
	"sort": [
		{"publish_date": {"order": "desc"}}
	]
}'
```

返回结果

```json
{
	"took": 54,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"skipped": 0,
		"failed": 0
	},
	"hits": {
		"total": 4,
		"max_score": null,
		"hits": [
			{
				"_index": "book",
				"_type": "noval",
				"_id": "iB4w53ABZ9Qw6KRQyuys",
				"_score": null,
				"_source": {
					"word_count": 98100,
					"author": "eeee",
					"title": "e3",
					"publish_date": "2010-3-2"
				},
				"sort": [
					1267488000000
				]
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "ih4353ABZ9Qw6KRQ_eyB",
				"_score": null,
				"_source": {
					"word_count": 91100,
					"author": "测试作者",
					"title": "测试书籍",
					"publish_date": "2005-3-2"
				},
				"sort": [
					1109721600000
				]
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "hx4v53ABZ9Qw6KRQW-yQ",
				"_score": null,
				"_source": {
					"word_count": 9800,
					"author": "ffff",
					"title": "ff2",
					"publish_date": "2001-3-2"
				},
				"sort": [
					983491200000
				]
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "iR4x53ABZ9Qw6KRQIOx-",
				"_score": null,
				"_source": {
					"word_count": 91100,
					"author": "dddd",
					"title": "d1",
					"publish_date": "1922-3-2"
				},
				"sort": [
					-1509580800000
				]
			}
		]
	}
}
```



聚合查询

对`word_count`和`publish_date`字段进行聚合查询，聚合分组自定义

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"aggs": {
		"group_by_word_count": {
			"terms": {
				"field": "word_count"
			}
		},
		"group_by_publish_date": {
			"terms": {
				"field": "publish_date"
			}
		}
	}
}'
```

返回结果

```json
{
	"took": 32,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"skipped": 0,
		"failed": 0
	},
	"hits": {
		"total": 4,
		"max_score": 1.0,
		"hits": [
			{
				"_index": "book",
				"_type": "noval",
				"_id": "ih4353ABZ9Qw6KRQ_eyB",
				"_score": 1.0,
				"_source": {
					"word_count": 91100,
					"author": "测试作者",
					"title": "测试书籍",
					"publish_date": "2005-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "hx4v53ABZ9Qw6KRQW-yQ",
				"_score": 1.0,
				"_source": {
					"word_count": 9800,
					"author": "ffff",
					"title": "ff2",
					"publish_date": "2001-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "iB4w53ABZ9Qw6KRQyuys",
				"_score": 1.0,
				"_source": {
					"word_count": 98100,
					"author": "eeee",
					"title": "e3",
					"publish_date": "2010-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "iR4x53ABZ9Qw6KRQIOx-",
				"_score": 1.0,
				"_source": {
					"word_count": 91100,
					"author": "dddd",
					"title": "d1",
					"publish_date": "1922-3-2"
				}
			}
		]
	},
	"aggregations": {
		"group_by_publish_date": {
			"doc_count_error_upper_bound": 0,
			"sum_other_doc_count": 0,
			"buckets": [
				{
					"key": -1509580800000,
					"key_as_string": "1922-03-02 00:03:00",
					"doc_count": 1
				},
				{
					"key": 983491200000,
					"key_as_string": "2001-03-02 00:03:00",
					"doc_count": 1
				},
				{
					"key": 1109721600000,
					"key_as_string": "2005-03-02 00:03:00",
					"doc_count": 1
				},
				{
					"key": 1267488000000,
					"key_as_string": "2010-03-02 00:03:00",
					"doc_count": 1
				}
			]
		},
		"group_by_word_count": {
			"doc_count_error_upper_bound": 0,
			"sum_other_doc_count": 0,
			"buckets": [
				{
					"key": 91100,
					"doc_count": 2
				},
				{
					"key": 9800,
					"doc_count": 1
				},
				{
					"key": 98100,
					"doc_count": 1
				}
			]
		}
	}
}
```



对`word_count`字段聚合函数计算，包括sum，avg，min，max，stats（包含前面所有的功能）

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"aggs": {
		"grades_word_count": {
			"stats": {
				"field": "word_count"
			}
		}
	}
}'
```

返回值

```json
{
	"took": 47,
	"timed_out": false,
	"_shards": {
		"total": 5,
		"successful": 5,
		"skipped": 0,
		"failed": 0
	},
	"hits": {
		"total": 4,
		"max_score": 1.0,
		"hits": [
			{
				"_index": "book",
				"_type": "noval",
				"_id": "ih4353ABZ9Qw6KRQ_eyB",
				"_score": 1.0,
				"_source": {
					"word_count": 91100,
					"author": "测试作者",
					"title": "测试书籍",
					"publish_date": "2005-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "hx4v53ABZ9Qw6KRQW-yQ",
				"_score": 1.0,
				"_source": {
					"word_count": 9800,
					"author": "ffff",
					"title": "ff2",
					"publish_date": "2001-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "iB4w53ABZ9Qw6KRQyuys",
				"_score": 1.0,
				"_source": {
					"word_count": 98100,
					"author": "eeee",
					"title": "e3",
					"publish_date": "2010-3-2"
				}
			},
			{
				"_index": "book",
				"_type": "noval",
				"_id": "iR4x53ABZ9Qw6KRQIOx-",
				"_score": 1.0,
				"_source": {
					"word_count": 91100,
					"author": "dddd",
					"title": "d1",
					"publish_date": "1922-3-2"
				}
			}
		]
	},
	"aggregations": {
		"grades_word_count": {
			"count": 4,
			"min": 9800.0,
			"max": 98100.0,
			"avg": 72525.0,
			"sum": 290100.0
		}
	}
}
```



## ES高级查询

### 子条件查询

#### Query context

> 在查询过程中，除了判断文档是否满足查询条件，ES还会计算一个_score来标识匹配的程度，旨在判断目标文档和查询条件匹配的有多好

* 全文本查询：针对文本类型数据

  模糊匹配查询

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

  默认会逐个字（分词匹配）匹配，要把`author`作为一个整体查询

  ```shell
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"match_phrase": {
  			"author": "eeee"
  		}
  	}
  }'
  ```

  多个字段模糊匹配查询。查询内容为`测试`，搜搜的字段为`author`和`title`

  ```shell
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"multi_match": {
  			"query": "测试",
  			"fields": ["author", "title"]
  		}
  	}
  }'
  ```

  语法查询，使用`AND`和`OR`来写条件

  ```shell
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"query_string": {
  			"query": "(测试 AND A) OR b"
  		}
  	}
  }'
  
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"query_string": {
  			"query": "(测试 AND A) OR b",
  			"fields": ["title", "author"]
  		}
  	}
  }'
  ```

  

* 字段级别查询：针对结构化数据，如数字，日期等

  查询`word_count`为1000的文档。查询`author`为eeee的文档

  ```shell
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"term": {
  			"word_count": 1000
  		}
  	}
  }'
  
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"term": {
  			"author": "eeee"
  		}
  	}
  }'
  ```

  查询`word_count`大于等于1000小于等于2000的文档

  ```shell
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"range": {
  			"word_count": {
  				"gte": 1000,
  				"lte": 100000
  			}
  		}
  	}
  }'
  ```

  > gte：大于等于
  >
  > gt：大于
  >
  > lt：小于
  >
  > lte：小于等于

  也可以用在日期上

  ```shell
  curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"query": {
  		"range": {
  			"publish_date": {
  				"gte": "2010-1-1",
  				"lte": "now"
  			}
  		}
  	}
  }'
  ```

  

#### Filter context

filter会被缓存

查询`word_count`为1000的文档

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"bool": {
			"filter": {
				"term": {
					"word_count": 1000
				}
			}
		}
	}
}'
```



### 复合条件查询

#### 固定分数查询

给每个查询到的文档都设置`_score`为2

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"constant_score": {
			"filter": {
				"match": {
					"title": "测试"
				}
			},
			"boost": 2
		}
	}
}'
```



#### 布尔查询

满足`author`是e或者`title`是eeee的就会被查询到

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"bool": {
			"should": [
				{"match": {"author": "e"}},
				{"match": {"title": "eeee"}}
			]
		}
	}
}'
```

满足两个条件

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"bool": {
			"must": [
				{"match": {"author": "e"}},
				{"match": {"title": "eeee"}}
			]
		}
	}
}'
```

也可以再加过滤条件

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"bool": {
			"must": [
				{"match": {"author": "e"}},
				{"match": {"title": "eeee"}}
			],
			"filter": [
				{"term": {"word_count": 1000}}	
			]
		}
	}
}'
```

一定不能满足某个条件

```shell
curl --location --request POST '10.10.10.246:9200/book/noval/_search' \
--header 'Content-Type: application/json' \
--data-raw '{
	"query": {
		"bool": {
			"must_not": [
				{"match": {"author": "e"}},
				{"match": {"title": "eeee"}}
			]
		}
	}
}'
```

