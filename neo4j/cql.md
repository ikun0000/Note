# CQL语言

CQL代表Cypher查询语言。 像Oracle数据库具有查询语言SQL，Neo4j具有CQL作为查询语言。



## 创建节点

创建节点的语法如下：

```CQL
CREATE (<节点名>:<标签名>)
```

这是创建一个没有任何属性的节点，如果需要添加属性的语法如下：

```CQL
CREATE (
	<节点名>:<标签名>
    {
    	<属性1的key>:<属性1的value>
        <属性2的key>:<属性2的value>
    	...
        <属性n的key>:<属性n的value>
    }
)
```



例如创建一个没有属性的节点：

```CQL
CREATE (student:ClassA)
```

创建一个带有属性的节点：

```CQL
CREATE (teacher:OfficeA {sal:10000})
```

> `shift` + `Enter` 换行