# DML(Data Manipulation Language)语句



## 插入语句

指定插入字段

```shell
mysql> INSERT INTO student (name, intro) 
    -> VALUES ('name1', 'intro1');
Query OK, 1 row affected (0.00 sec)

```



不指定插入字段就是按顺序插入

```shell
mysql> INSERT INTO department VALUES (9527, 'dev', 'coding');
Query OK, 1 row affected (0.01 sec)

```



VALUE 后跟多个括号可以批量插入

```shell
mysql> INSERT INTO department 
    -> VALUES (9999, 'ops', 'track'),
    -> (8888, 'boss', 'f**k'),
    -> (7777, 'market', 'beautiful');
Query OK, 3 rows affected (0.01 sec)
Records: 3  Duplicates: 0  Warnings: 0

```



## 修改语句



### 全部修改

```shell
mysql> UPDATE department SET intro='no intro', name=concat(name, '_all');
Query OK, 4 rows affected (0.00 sec)
Rows matched: 4  Changed: 4  Warnings: 0

```

`concat()`函数用于连接字符串



### 带条件的修改

```shell
mysql> UPDATE department SET intro='f**k' WHERE id=8888;
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

```

`WHERE` 子句可以带上条件，可以使用一般的比较运算符(=, >=, <=, !=)

还支持逻辑运算符(AND, OR, NOT)

或者是模糊匹配`LIKE '%boss%'`（`%`代表匹配0个或多个字符，`_`代表匹配一个字符）



也可以制定一个范围

```shell
mysql> UPDATE department SET intro='good department' WHERE id BETWEEN 8000 AND 9600;
Query OK, 2 rows affected (0.01 sec)
Rows matched: 2  Changed: 2  Warnings: 0

```

`BETWEEN`用于指定范围，他相当于下面的语句

```shell
mysql> UPDATE department SET intro='good department' WHERE id>=8000 AND id<=9600;
Query OK, 2 rows affected (0.01 sec)
Rows matched: 2  Changed: 2  Warnings: 0

```

`BETWEEN`也可以比较日期，例如

```sql
BETWEEN '2001-01-01' AND '2020-01-01'
```



指定在某个集合内，或者不在某个集合内

```shell
mysql> UPDATE department SET intro='bad department' WHERE id IN (7777, 8888);
Query OK, 2 rows affected (0.01 sec)
Rows matched: 2  Changed: 2  Warnings: 0

mysql> UPDATE department SET intro='bad department' WHERE id NOT IN (7777, 8888);
Query OK, 2 rows affected (0.00 sec)
Rows matched: 2  Changed: 2  Warnings: 0

```

当然，`IN`后面也可以嵌套另外一个SQL语句

```sql
UPDATE student SET name='' WHERE id IN (SELECT id FROM department);
```



## 删除语句

删除所有记录

```shell
mysql> DELETE FROM student;
Query OK, 1 row affected (0.00 sec)

mysql> TRUNCATE student;
Query OK, 0 rows affected (0.02 sec)

```

TRUNCATE会把`AUTO_INCREMENT`的字段重置为0



带条件的删除

```shell
mysql> DELETE FROM department WHERE name LIKE '%boss%';
Query OK, 1 row affected (0.01 sec)

```

和`UPDATE`语句一样也可以带`WHERE`字句来删除特定记录，条件也是一样的



删除某一列为空记录的行

```shell
mysql> DELETE FROM department WHERE name IS NULL;
Query OK, 0 rows affected (0.00 sec)

```

**不能使用下面的**

```shell
mysql> DELETE FROM department WHERE name=NULL;
Query OK, 0 rows affected (0.00 sec)

```

因为这样`name=NULL`本来就是false的



删除某个字段不为空的行

```shell
mysql>  DELETE FROM department WHERE name IS NOT NULL;
Query OK, 3 rows affected (0.00 sec)

```

**这些WHERE字句可以用到上面的 `UPDATE`  和 `SELECT` 语句中**