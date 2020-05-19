# MySQL索引

数据本社之外，数据库还维护着一个满足特定查找算法的数据结构（MySQL一般时B-Tree），这些数据结构以某种方式指向数据，这样就可以在这些数据结构的基础上实现高级的查找算法，这种数据结构就是索引

普通索引的数据结构时B-Tree



## 查看某张表的索引

```sql
mysql> SHOW INDEX FROM department;
+------------+------------+-------------------------------+--------------+----------------+-----------+-------------+----------+--------+------
+------------+---------+---------------+---------+------------+
| Table      | Non_unique | Key_name                      | Seq_in_index | Column_name    | Collation | Cardinality | Sub_part | Packed | Null 
| Index_type | Comment | Index_comment | Visible | Expression |
+------------+------------+-------------------------------+--------------+----------------+-----------+-------------+----------+--------+------
+------------+---------+---------------+---------+------------+
| department |          0 | PRIMARY                       |            1 | id             | A         |           0 |     NULL |   NULL |      
| BTREE      |         |               | YES     | NULL       |
| department |          1 | idx_department_departmentName |            1 | departmentName | A         |           0 |     NULL |   NULL | YES  
| BTREE      |         |               | YES     | NULL       |
+------------+------------+-------------------------------+--------------+----------------+-----------+-------------+----------+--------+------
+------------+---------+---------------+---------+------------+
2 rows in set (0.00 sec)

```



## 不需要创建索引的情况

* 表记录太少

* 经常增删改的列（索引会降低`INSERT`,`UPDATE`,`DELETE`的效率）

* 数据重复且分布平均的字段，因此只为最经常查询和经常排序的数据列建立索引。***注意，如果某个数据列包含许多重复的内容，为它建立索引就没有太大实际效果***

  > 假如一张表有10w行记录，有一个字段field只有True和False两种状态，且每个值的分布概率大约为50%，那么对这个列创建索引一般不会提高数据库的查询速度。
  >
  > 索引的选择性是指索引列中不同值的数目与表中记录数的比，如果一个表有2000行记录，表索引列有1980个不同的值，那么这个索引的选择性就是1980/2000=0.99。一个索引的选择性越接近1，这个索引的效率就越高。



## MySQL索引分类

### 创建索引

```sql
CREATE [UNIQUE] INDEX indexName ON tableName(columnName(length));
```

```sql
ALTER TABLE tableName ADD [UNIQUE] INDEX indexName ON (columnName(length));
```



### 单值索引

即一个索引包含单个列，一个表可以有多个单列索引

**创建单值索引**

```sql
mysql> CREATE INDEX idx_department_departmentName ON department(departmentName);
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0

```

**创建表时新建索引**

```sql
mysql> CREATE TABLE student (
    -> id BIGINT AUTO_INCREMENT,
    -> name CHAR(30) NOT NULL,
    -> PRIMARY KEY (id),
    -> KEY idx_name (name)
    -> );
Query OK, 0 rows affected (0.02 sec)

```



### 唯一索引

索引列的值必须唯一，但允许为空值

**创建唯一键索引**

```sql
mysql> CREATE TABLE student (
    -> id BIGINT AUTO_INCREMENT,
    -> name CHAR(30) NOT NULL,
    -> PRIMARY KEY (id),
    -> UNIQUE INDEX unique_name (name)
    -> );
Query OK, 0 rows affected (0.01 sec)

```

```sql
mysql> CREATE UNIQUE INDEX idx_departmentName ON department(departmentName);
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



### 复合索引

一个索引包含多个列

**创建多值索引**

```sql
mysql> CREATE INDEX idx_employee_lastName_email ON employee(lastName, email);
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



### 哈希索引

普通索引使用B-Tree维护索引，而哈希索引使用Hash表，普通索引的查找时间复杂度为O(log n)，而哈希索引的查找时间复杂度为O(1)

**创建表时添加哈希索引**

```sql
mysql> CREATE TABLE student (
    -> id BIGINT PRIMARY KEY AUTO_INCREMENT,
    -> nam CHAR(30) NOT NULL,
    -> KEY USING HASH(nam)
    -> );
Query OK, 0 rows affected, 1 warning (0.01 sec)

```





## 删除索引

```sql
mysql> DROP INDEX idx_employee_lastName_email ON employee;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

```
