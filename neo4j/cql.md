# CQL语言

CQL代表Cypher查询语言。 像Oracle数据库具有查询语言SQL，Neo4j具有CQL作为查询语言。



> 在neo4j的web控制台中按 `shift` + `Enter` 换行
>
> 显示所有命令帮助：`:help cypher`
>
> 显示一个命令的命令帮助：`:help <command>`



1. Neo4j使用节点名将节点详细信息存储在Database.As中作为Neo4j DBA或Developer，我们不能使用它来访问节点详细信息。
2. Neo4j创建一个节点标签作为内部节点名称的别名。作为Neo4j DBA或Developer，我们应该使用此标签名称来访问节点详细信息。



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

**neo4j中可以有多个相同节点名和标签名的节点，不过他们的 `identity` 是不同的。**

Label是Neo4j数据库中的节点或关系的名称或标识符，可以将此标签名称称为关系为“关系类型”。

创建带有多个标签的节点：

```CQL
CREATE (<节点名>:<标签名1>:<标签名2>:...<标签名n>)
```

例如创建一个没有属性的节点：

```CQL
CREATE (student:Student)
```

创建一个带有属性的节点：

```CQL
CREATE (teacher:Teacher {sal:10000})
```



## 获取节点

获取节点需要使用 `MATCH` 和 `RETURN` 语句，单独使用 `MATCH` 和 `RETURN` 会报 ` Neo.ClientError.Statement.SyntaxError ` 错误。

`RETURN` 是一个字句，可以跟在 `CREATE`，`DELETE`，`MATCH` 语句后面。

语法如下：

```CQL
MATCH (<节点名1>:<标签名1>), (<节点名2>:<标签名2>), ... (<节点名n>:<标签名n>)
RETURN 节点名1, 节点名2, ... 节点名m， [节点名n.属性名]
```

```CQL
MATCH (<节点名1>), (<节点名2>), ... (<节点名n>)
RETURN 节点名1, 节点名2, ... 节点名m， [节点名n.属性名]
```

`RETURN` 可以使用 `AS` 给属性名起别名：

```CQL
MATCH (<节点名>), (<节点名>)
RETURN 节点名.属性1 AS 别名1， 节点名.属性2 AS 别名2， ... 节点名.属性n AS 别名n
```



例如：

```CQL
MATCH (student:Student) 
RETURN student
```

```CQL
MATCH (teacher:Teacher), (student:Student) 
RETURN student, teacher
```

```CQL
MATCH (student:Student) 
RETURN student.name, student.age
```



## 删除节点

删除节点使用 `DELETE` 字句，他无法单独使用，必须和 `MATCH` 一起使用：

```CQL
MATCH (<节点名>:<节点标签>) 
DELETE <节点名>
```

也可以跟着 `WHERE` 一起使用：

```CQL
MATCH (<节点名>:<节点标签>) 
WHERE <删除节点的条件>
DELETE <节点名>
```



例如：

删除一个节点

```CQL
MATCH (test:Test) 
DELETE test
```

有条件的删除节点

```CQL
MATCH (test:Test) 
WHERE test.p2 = "v2" 
DELETE test
```



## 添加/删除节点的属性

neo4j支持动态增加/删除节点属性的值，语法如下：

```CQL
MATCH (<节点名>:<节点标签>)
REMOVE|SET <节点名.属性1>, <节点名.属性2>, ... <节点名.属性n>
```



例如删除一个属性：

```CQL
MATCH (test:Test) 
REMOVE test.p1
```

添加一个属性：

```CQL
MATCH (test:Test) 
SET test.p3 = "v3"
```

**`SET` 除了添加属性之外还可以修改属性：**

```CQL
MATCH (test:Test) SET test.p3 = "v4"
```



## 创建、删除和查询关系

创建关系大概分两种情况：在现有节点的基础上创建关系，新增节点并创建关系

**可以在两个节点上创建多条同名同标签的关系，他们通过 `identity` 区分**

### 在现有节点上增加关系

```CQL
MATCH (<节点名1>:<标签名1>), (<节点名1>:<标签名1>)
CREATE (节点名1)-[<关系名>:<关系标签>]->(节点名2)
```

```CQL
MATCH (<节点名1>:<标签名1>), (<节点名1>:<标签名1>)
CREATE (节点名1)-[<关系名>:<关系标签> {属性名1: 属性值1， 属性名1: 属性值1， ... 属性名n: 属性值n}]->(节点名2)
```

也可以在后面添加 `RETURN` 语句来返回新建的关系



例如：

```CQL
MATCH (teacher:Teacher), (student:Student) 
CREATE (teacher)-[teach:Teach]->(student)
```

如果要同时返回结果加上 `RETURN` 语句即可：

```CQL
MATCH (teacher:Teacher), (student:Student) 
CREATE (teacher)-[teach:Teach]->(student)
RETURN teacher, teach, student
```



### 新增节点并添加关系

创建节点是添加关系的语法如下：

```CQL
CREATE (<节点名1>:<标签名1>)-[<关系名>:<关系标签>]->(<节点名2>:<关系名2>)
```

```CQL
CREATE (<节点名1>:<标签名1>)-[<关系名>:<关系标签> {属性名1: 属性值1， 属性名1: 属性值1， ... 属性名n: 属性值n}]->(<节点名2>:<关系名2>)
```

也可以在后面添加 `RETURN` 语句来返回新建的关系



例如：

```CQL
CREATE (staff:Staff)-[workfor:Workfor {workhour: 8}]->(boss:Boss) 
RETURN staff, workfor, boss
```



### 查看节点的关系

查询的语法如下：

```cq
MATCH (<节点名1>:<节点标签1>)-[<关系名>:<关系标签>]->(<节点名2>:<节点标签2>) 
RETURN <节点名1>, <节点名2>, <关系名1>
```

```cq
MATCH (<节点名1>)-[<关系名>:<关系标签>]->(<节点名2>) 
RETURN <节点名1>, <节点名2>, <关系名1>
```



例如：

```CQL
MATCH (teacher:Teacher)-[teach:Teach]->(student:Student) 
RETURN student, teach, teacher
```

```CQL
MATCH (staff:Staff)-[workfor:Workfor]->(boss:Boss) 
RETURN workfor
```



### 删除节点和节点的关系

删除关系也是需要使用 `MATCH` 和 `DELETE` 语句完成，语法如下：

```c
MATCH (<节点名1>:<节点标签1>)-[<关系名>:<关系标签>]->(<节点名2>:<节点标签2>)
DELETE 关系名
```

```CQL
MATCH (<节点名1>:<节点标签1>)-[<关系名>:<关系标签>]->(<节点名2>:<节点标签2>)
DELETE 节点名1, 关系名, 节点名2
```



例如：

```CQL
MATCH (staff:Staff)-[workfor:Workfor]->(boss:Boss) 
DELETE staff, workfor, boss
```

```CQL
MATCH (teacher:Teacher)-[teach:Teach]->(student:Student) 
DELETE teach
```



## 条件查询

neo4j的 `MATCH` 查询可以像SQL一样带 `WHERE` 来设置查询条件，他的语法如下：

```CQL
MATCH (<节点名>:<节点标签>)
WHERE <条件表达式>
RETURN <节点名或者节点名.属性>
```

neo4j支持四种布尔运算符：

* `AND`
* `OR`
* `NOT`
* `XOR`

支持一下比较云算法：

* `=`
* `<>`
* `<`
* `>`
* `<=`
* `>=`

NULL值

* `IS NULL`
* `IS NOT NULL`

匹配多个结果

* `IN [目标值1, 目标值2, 目标值3, ... 目标值n]`



例如：查询工资大于4000的节点：

```CQL
MATCH (employee:Employee) 
WHERE employee.wage > 4000 
RETURN employee
```

表达式和SQL的一样



## 排序

结合 `MATCH` 和 `RETURN` 的语法如下：

```CQL
MATCH (<节点名>:<节点标签>)
RETURN 节点名
ORDER BY 节点名.排序的字段 [DESC]
```



例如按照级别升序排序：

```CQL
MATCH (employee:Employee) 
RETURN employee 
ORDER BY employee.level
```

按照级别降序序排序：

```CQL
MATCH (employee:Employee) 
RETURN employee 
ORDER BY employee.level DESC
```

对数字类型字段排序：

```CQL
MATCH (employee:Employee) 
RETURN employee 
ORDER BY employee.wage DESC
```



## 分页

分页语法如下：

```CQL
MATCH (<节点名>:<节点标签>)
RETURN <节点名>
SKIP <从返回的第n项开始查找>
LIMIT <查找m项>
```

当然在 `MATCH` 和 `RETURN` 语句之间也可以加上 `WHERE` 语句来限制查询的节点



例如：

```CQL
MATCH (employee:Employee)
RETURN employee
SKIP 2
LIMIT 2
```



## 联合查询

neo4j使用 `UNION` 和 `UNION ALL` 来查询，效果和SQL中的一样

* `UNION` 用来合并两个结果集中的公共集合，并且不返回相同的行
* `UNION ALL` 也是返回两个结果集中的公共集合，他会返回所有行，包括重复行

语法如下：

```CQL
MATCH <查询语句>
UNION|UNION ALL
MATCH <查询语句>
```



例如：

```CQL
MATCH (employee:Employee) RETURN employee.name, employee.wage, employee.level
UNION 
MATCH (manager:Manager) RETURN manager.name, manager.wage, manager.level
```

这个查询会报 `Neo.ClientError.Statement.SyntaxError： All sub queries in an UNION must have the same column names` 错误，应为返回的列名不同，**那是因为他们的节点名前缀不同**，只需要加上 `AS` 来给列取别名即可：

```CQL
MATCH (employee:Employee) 
RETURN employee.name AS name, employee.wage AS wage, employee.level AS level
UNION
MATCH (manager:Manager) 
RETURN manager.name AS name, manager.wage AS wage, manager.level AS level
```



## 函数



### 普通函数

使用普通函数的语法如下：

```CQL
MATCH (<节点名>:<节点标签>)
RETURN 函数(节点名.属性1), 函数(节点名.属性2), ... 函数(节点名.属性n)
```

基本的函数如下：

* `UPPER`：把字符串转成大写
* `LOWER`：把字符串转成小写
* `SUBSTRING`：截取部分字符串
* `REPLACE`：替换字符串的一部分



例如：

```CQL
MATCH (employee:Employee) 
RETURN UPPER(employee.name)
```



### 聚合函数

语法和上面的一样，聚合函数包括：

* `COUNT`
* `MAX`
* `MIN`
* `SUM`
* `AVG`



里如：

```CQL
MATCH (employee:Employee) RETURN COUNT(*)
```

```CQL
MATCH (employee:Employee) 
RETURN MAX(employee.wage) AS max_wage, 
MIN(employee.wage) AS min_wage, 
AVG(employee.wage) AS avg_wage
```



### 关系函数

neo4j提供了一组关系函数，以在获取开始节点，结束节点等细节时知道关系的细节：

* ` STARTNODE `：获取关系的开始节点
* ` ENDNODE `：获取关系的结束节点
*  ` ID `：获取关系的ID
* ` TYPE `：字符串表示中的一个关系的TYPE

语法如下：

```CQL
MATCH (<节点名1>:<节点标签1>)-[<关系名>:<关系标签>]->(<节点名2>:<节点标签2>)
RETURN 关系函数(关系)
```



里如：

```CQL
MATCH (student:Student)-[learn:Learn]->(teacher:Teacher) 
RETURN ID(learn), STARTNODE(learn), ENDNODE(learn)
```



## 索引

neo4j支持节点或关系属性上的索引，以提高应用程序的性能。

创建索引：

```CQL
CREATE INDEX ON :<节点名> (<属性名>)
```

丢弃索引：

```CQL
DROP INDEX ON :<节点名> (<属性名>)
```



例如：

```CQL
CREATE INDEX ON :Manager (name)
```

```CQL
DROP INDEX ON :Manager (name)
```



## UNIQUE约束

neo4j在创建节点和关系的时候允许存在多个同名同标签同属性的节点或关系，如果不希望这样可以使用UNIQUE来做唯一约束来禁止插入同属性的节点：

添加UNIQUE约束：

```CQL
CREATE CONSTRAINT ON (<节点名>:<标签名>)
ASSERT <节点名.属性名> IS UNIQUE
```

删除UNIQUE约束：

```CQL
DROP CONSTRAINT ON (<节点名>:<标签名>)
ASSERT <节点名.属性名> IS UNIQUE
```



例如：

```CQL
CREATE CONSTRAINT ON (student:Student) 
ASSERT student.id IS UNIQUE
```

```CQL
DROP CONSTRAINT ON (student:Student) 
ASSERT student.id IS UNIQUE
```

