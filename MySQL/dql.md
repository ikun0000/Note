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



## 子查询

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