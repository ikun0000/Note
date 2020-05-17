# DQL(Data Query Language)语句

## 查询所有内容

```sql
SELECT * FROM book;
```



## 查询指定的列

```sql
SELECT book_name, author FROM book;
```



## 去除重复列

```sql
SELECT DISTINCT author FROM book;
```

找出所有作者



```sql
SELECT DISTINCT * FROM book;
```

去除重复行



```sql
SELECT DISTINCT author, description FROM book;
```

查询author，description组成的行并且不重复



## 列运算

```sql
SELECT book_name, price * 0.8 FROM book;
```

显示把所有书打8折后的书名和价格，当然也可以使用别的四则运算（`+`, `-`, `*`, `/`）



```sql
SELECT book_name, CONCAT(isbn, '_A') FROM book;
```

查看ISBN后面连接一个字符串，`CONCAT(string1, string2,...)`用于连接多个字符串

`IFNULL(col, replace value)`用于在查询出来空的时候用已知的值代替



## 给查询出来的列起别名

```sql
SELECT book_name AS BookName, author AS Author FROM book;
```

把book_name替换为Book Name，author替换为Author



## 条件控制

````sql
SELECT * FROM book WHERE price > 80;
````

查询价格在80元以上的书，可以使用（`<`, `>`, `<=`, `>=`, `!=`）

也可以配合使用逻辑运算（`AND`, `OR`, `NOT`）

```sql
SELECT * FROM book WHERE price < 100 AND price > 50;
```
上卖弄这种如果包含50和100的话也可以使用`BETWEEN`来完成
```sql
SELECT * FROM book WHERE price BETWEEN 50 AND 100;
```

`BETWEEN`中也可以跟时间，`BETWEEN '2001-01-01' AND '2020-01-01'`



```sql
SELECT * FROM book WHERE author='Robert Sedgewick' OR author='Bruce Eckel';
```

当然，除了上面这种查询指定作者还有另外一种方法

```sql
SELECT * FROM book WHERE author IN ('Robert Sedgewick', 'Bruce Eckel');
```



还有查询为空列或者不为空列的记录

```sql
SELECT * FROM book WHERE author IS NULL;
```

```sql
SELECT * FROM book WHERE author IS NOT NULL;
```



## 模糊查询

```sql
SELECT * FROM book WHERE book_name LIKE '%Java%';
```

查询书名中有Java的书，不区分大小写

```sql
SELECT * FROM book WHERE book_name LIKE '%Java';
```

这个是查询以Java开头的书

```sql
SELECT * FROM book WHERE book_name LIKE 'Java%';
```

这个是查询以Java结尾的书

**上面三个例子中的`%`代表匹配0个或多个字符**



如果只需要匹配一个字符

```sql
SELECT * FROM book WHERE isbn LIKE '978-7-111-64124-_';
```

```sql
SELECT * FROM book WHERE description LIKE 'J_M';
```

`_`指定要匹配一个任意字符



## 结果排序

```sql
SELECT * FROM book ORDER BY price DESC;
```

```sql
SELECT * FROM book ORDER BY price ASC;
```

上面分别对price进行降序（DESC）和升序（ASC）排序（默认是升序）



设置多个排序条件

```sql
SELECT * FROM book ORDER BY price DESC, isbn ASC;
```

首先以price降序排列，如果price相同则将price相同的记录以isbn升序



## 聚合函数

统计行数

```sql
SELECT count(*) FROM book;
```



最大值/最小值

```sql
SELECT MAX(price), MIN(price) FROM book;
```



求和/平均值

```sql
SELECT SUM(price), AVG(price) FROM book;
```



## 分组查询

```
+----+----------+-----+-----+------+
| id | name     | age | sex | math |
+----+----------+-----+-----+------+
|  1 | xiaoming |  15 |   1 |   91 |
|  2 | xiaohua  |  14 |   0 |   99 |
|  3 | xiaohua  |  17 |   1 |  100 |
|  4 | xiaohong |  12 |   0 |   80 |
|  5 | xiaobai  |  16 |   1 |   75 |
|  6 | xiaomei  |  13 |   0 |   80 |
+----+----------+-----+-----+------+
```

```sql
mysql> SELECT sex, max(age), min(age) FROM student GROUP BY sex;
+-----+----------+----------+
| sex | max(age) | min(age) |
+-----+----------+----------+
|   1 |       17 |       15 |
|   0 |       14 |       12 |
+-----+----------+----------+
2 rows in set (0.00 sec)

```

把学生表用sex分开，统计每个sex的最大年龄，最小年龄

***SELECT后面只能跟分组列和聚合函数***



### WHERE 和 HAVING

分组使用`WHERE`之后，他会在**分组之前**先过滤掉不符合条件的，保留符合条件的进行分组（即先过滤，后分组）

```sql
mysql> SELECT sex, max(math), min(math) FROM student GROUP BY sex;
+-----+-----------+-----------+
| sex | max(math) | min(math) |
+-----+-----------+-----------+
|   1 |       100 |        75 |
|   0 |        99 |        80 |
+-----+-----------+-----------+
2 rows in set (0.00 sec)

mysql> SELECT sex, max(math), min(math) FROM student WHERE math <= 90 GROUP BY sex;
+-----+-----------+-----------+
| sex | max(math) | min(math) |
+-----+-----------+-----------+
|   0 |        80 |        80 |
|   1 |        75 |        75 |
+-----+-----------+-----------+
2 rows in set (0.01 sec)

```



`HAVING`字句则是在分组之后再进行过滤的，并且条件必须是聚合函数，不能使用单独列（先分组，后过滤）

```sql
mysql> SELECT sex, sum(math) FROM student GROUP BY sex;
+-----+-----------+
| sex | sum(math) |
+-----+-----------+
|   1 |       266 |
|   0 |       259 |
+-----+-----------+
2 rows in set (0.00 sec)

mysql> SELECT sex, sum(math) FROM student GROUP BY sex HAVING sum(math) > 260;
+-----+-----------+
| sex | sum(math) |
+-----+-----------+
|   1 |       266 |
+-----+-----------+
1 row in set (0.00 sec)
```

>  `WHERE`和`HAVING`的区别在于分组查询，如果`HAVING`字句单独存在，那么他和`WHERE`字句的效果一样



### 执行顺序

```
SELECT  ->  FROM  ->  WHERE  ->  GROUP BY  ->  HAVING  ->  ORDER BY  ->  LIMIT
```



## 分页查询

```
+----+----------+-----+-----+------+
| id | name     | age | sex | math |
+----+----------+-----+-----+------+
|  1 | xiaoming |  15 |   1 |   91 |
|  2 | xiaohua  |  14 |   0 |   99 |
|  3 | xiaohua  |  17 |   1 |  100 |
|  4 | xiaohong |  12 |   0 |   80 |
|  5 | xiaobai  |  16 |   1 |   75 |
|  6 | xiaomei  |  13 |   0 |   80 |
+----+----------+-----+-----+------+
```

```sql
mysql> SELECT * FROM student LIMIT 2, 2;
+----+----------+-----+-----+------+
| id | name     | age | sex | math |
+----+----------+-----+-----+------+
|  3 | xiaohua  |  17 |   1 |  100 |
|  4 | xiaohong |  12 |   0 |   80 |
+----+----------+-----+-----+------+
2 rows in set (0.00 sec)

mysql> SELECT * FROM student LIMIT 2, 3;
+----+----------+-----+-----+------+
| id | name     | age | sex | math |
+----+----------+-----+-----+------+
|  3 | xiaohua  |  17 |   1 |  100 |
|  4 | xiaohong |  12 |   0 |   80 |
|  5 | xiaobai  |  16 |   1 |   75 |
+----+----------+-----+-----+------+
3 rows in set (0.00 sec)

```

查询第2行（从0开）的数据，并且只查询3个记录



## 多表查询

```
mysql> desc book;
+-------------+---------------+------+-----+---------+----------------+
| Field       | Type          | Null | Key | Default | Extra          |
+-------------+---------------+------+-----+---------+----------------+
| id          | int           | NO   | PRI | NULL    | auto_increment |
| book_name   | varchar(50)   | NO   |     | NULL    |                |
| author      | varchar(50)   | NO   |     | NULL    |                |
| price       | decimal(10,2) | NO   |     | NULL    |                |
| description | varchar(100)  | YES  |     | NULL    |                |
| isbn        | varchar(50)   | NO   |     | NULL    |                |
+-------------+---------------+------+-----+---------+----------------+
6 rows in set (0.00 sec)

mysql> desc student;
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| id    | bigint      | NO   | PRI | NULL    | auto_increment |
| name  | varchar(50) | YES  |     | NULL    |                |
| age   | tinyint     | NO   |     | NULL    |                |
| sex   | tinyint     | NO   |     | NULL    |                |
| math  | tinyint     | YES  |     | 0       |                |
+-------+-------------+------+-----+---------+----------------+
5 rows in set (0.00 sec)

```



### 表别名

```sql
mysql> SELECT stu.id, stu.name, s.score FROM student stu INNER JOIN sc s ON stu.student_id=s.id;
+----+------+-------+
| id | name | score |
+----+------+-------+
|  1 | a    |    20 |
|  0 | b    |    30 |
+----+------+-------+
2 rows in set (0.00 sec)

```

只需要在`FROM`后面的表名跟上别名就可以在语句任意位置使用别名



### 合并结果集

```sql
SELECT id, book_name FROM book 
UNION 
SELECT id, name FROM student;
```

这样会把book的id，book_name和student的id，name合并为一个结果集

**使用`UNION`需要多张表返回的对应列的数据类型，列的数量一致**



直接使用`UNION`时完全相同的行会被去除，如果不想去除使用`UNION ALL`

```sql
SELECT id, book_name FROM book 
UNION ALL
SELECT id, name FROM student;
```



### 连接查询

```sql
mysql> select * from student;
+----+------------+------+
| id | student_id | name |
+----+------------+------+
|  0 |         12 | b    |
|  1 |         11 | a    |
|  3 |         13 | c    |
+----+------------+------+
3 rows in set (0.00 sec)

mysql> select * from sc;
+----+-------+
| id | score |
+----+-------+
| 10 |    40 |
| 11 |    20 |
| 12 |    30 |
+----+-------+
3 rows in set (0.00 sec)

```

如果直接查询两张表会得到一个笛卡儿积，sc表中的每一列会和student表中的每一列组成新的一列

```sql
mysql> SELECT * FROM sc, student;
+----+-------+----+------------+------+
| id | score | id | student_id | name |
+----+-------+----+------------+------+
| 12 |    30 |  0 |         12 | b    |
| 11 |    20 |  0 |         12 | b    |
| 10 |    40 |  0 |         12 | b    |
| 12 |    30 |  1 |         11 | a    |
| 11 |    20 |  1 |         11 | a    |
| 10 |    40 |  1 |         11 | a    |
| 12 |    30 |  3 |         13 | c    |
| 11 |    20 |  3 |         13 | c    |
| 10 |    40 |  3 |         13 | c    |
+----+-------+----+------------+------+
9 rows in set (0.00 sec)

```



#### 内连接

```sql
mysql> SELECT student.id, student_id, name, score FROM student INNER JOIN sc ON student.student_id=sc.id;
+----+------------+------+-------+
| id | student_id | name | score |
+----+------------+------+-------+
|  0 |         12 | b    |    30 |
|  1 |         11 | a    |    20 |
+----+------------+------+-------+
2 rows in set (0.00 sec)

```

 也就是在笛卡尔积中查找student.student_id和sc.id相同的行

也可以简写成

```sql
SELECT student.id, student_id, name, score 
FROM student, sc 
WHERE student.student_id=sc.id;
```

其中`FROM`不一定要是表名，也可以是一个`SELECT`语句，那么他会把`SELECT`语句查询结果当成一张表。`FROM`后面的多张表可以是同一张表



#### 外连接



##### 左外连接

```sql
mysql> SELECT student.id, student_id, name, sc.score FROM student LEFT JOIN sc ON sc.id=student.student_id;
+----+------------+------+-------+
| id | student_id | name | score |
+----+------------+------+-------+
|  0 |         12 | b    |    30 |
|  1 |         11 | a    |    20 |
|  3 |         13 | c    |  NULL |
+----+------------+------+-------+
3 rows in set (0.00 sec)

```

使用`LEFT JOIN`时一定会列出`LEFT JOIN`左边那张表的全部内容，然后根据`ON`的条件连接两张表的内容，如果返现右边表没有能匹配左边表的内容则为NULL



##### 右外连接

```sql
mysql> SELECT sc.id, score, student.id, name FROM student RIGHT JOIN sc ON sc.id=student.student_id;
+----+-------+------+------+
| id | score | id   | name |
+----+-------+------+------+
| 10 |    40 | NULL | NULL |
| 11 |    20 |    1 | a    |
| 12 |    30 |    0 | b    |
+----+-------+------+------+
3 rows in set (0.00 sec)

```

右连接和左连接相反，他会先输出`RIGHT JOIN`右边表的所有行，然后就是根据`ON`条件用左表内容连接到右表上，如果`ON`没有匹配到左表对应的内容则为NULL



##### 全外连接

全外连接就是左右不满做`ON`条件都列出来，但是MySQL不支持，但是可以用`UNION`配合`LEFT JOIN`,`RIGHT JOIN`模拟

```sql
mysql> SELECT student.id, student_id, name, score FROM student LEFT JOIN sc ON student.student_id=sc.id
    -> UNION
    -> SELECT student.id, student_id, name, score FROM student RIGHT JOIN sc ON student.student_id=sc.id;
+------+------------+------+-------+
| id   | student_id | name | score |
+------+------------+------+-------+
|    0 |         12 | b    |    30 |
|    1 |         11 | a    |    20 |
|    3 |         13 | c    |  NULL |
| NULL |       NULL | NULL |    40 |
+------+------------+------+-------+
4 rows in set (0.00 sec)

```



#### 自然连接

```sql
mysql>  SELECT student.id, student_id, name, score FROM student NATURAL JOIN sc;
Empty set (0.00 sec)

```

使用`NATURAL JOIN`他会自动去识别条件，即名字相同的列作为`ON`的条件



#### 多个表连接

```sql
SELECT t1.field1, t1.field2, t2.field3, t3.field4
FROM table1 t1 LEFT JOIN t2 ON t1.field5=t2.field5
			   LEFT JOIN t1 ON t1.field7=t1.field1
               LEFT JOIN t3 ON t2.field6=t3.field6
WHERE t1.fieldx<121;
```





### 子查询

```sql
mysql> SELECT * FROM student WHERE id IN (SELECT id FROM student WHERE math>=80);
+----+----------+-----+-----+------+
| id | name     | age | sex | math |
+----+----------+-----+-----+------+
|  1 | xiaoming |  15 |   1 |   91 |
|  2 | xiaohua  |  14 |   0 |   99 |
|  3 | xiaohua  |  17 |   1 |  100 |
|  4 | xiaohong |  12 |   0 |   80 |
|  6 | xiaomei  |  13 |   0 |   80 |
+----+----------+-----+-----+------+
5 rows in set (0.00 sec)

```

可以使用`IN`来嵌套另外一个SQL语句

