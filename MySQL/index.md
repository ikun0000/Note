# MySQL索引



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



## 创建单值索引

```sql
mysql> CREATE INDEX idx_department_departmentName ON department(departmentName);
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



## 创建多值索引

```sql
mysql> CREATE INDEX idx_employee_lastName_email ON employee(lastName, email);
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



## 删除索引

```sql
mysql> DROP INDEX idx_employee_lastName_email ON employee;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



## 创建表时新建索引

```sql
mysql> CREATE TABLE student (
    -> id BIGINT AUTO_INCREMENT,
    -> name CHAR(30) NOT NULL,
    -> PRIMARY KEY (id),
    -> KEY idx_name (name)
    -> );
Query OK, 0 rows affected (0.02 sec)

```



## 创建唯一键索引

```sql
mysql> CREATE TABLE student (
    -> id BIGINT AUTO_INCREMENT,
    -> name CHAR(30) NOT NULL,
    -> PRIMARY KEY (id),
    -> UNIQUE INDEX unique_name (name)
    -> );
Query OK, 0 rows affected (0.01 sec)

```

