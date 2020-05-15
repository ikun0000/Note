# DDL(Data Definiti on Language)语句



## 数据库操作

显示所有数据库名称

```shell
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.00 sec)

```



选择数据库/切换数据库

```shell
mysql> use mysql
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
```



创建数据库

```shell
mysql> CREATE DATABASE test;
Query OK, 1 row affected (0.01 sec)

```

这是简单的写法，完整的写法如下

```shell
mysql> CREATE DATABASE IF NOT EXISTS test
    -> CHARACTER SET utf8
    -> COLLATE utf8_general_ci;
Query OK, 1 row affected, 2 warnings (0.00 sec)

```

`IF NOT EXISTS`用来保证在数据库不存在时候才创建，避免报错

`CHARACTER SET <字符集名>`用来设置这本数据库使用的字符集，建议全部使用utf8（注意没有`-`）

`COLLATE <校对规则名>`设置校对规则，如果使用utf8字符集就使用utf8_general_ci



删除数据库

```shell
mysql> DROP DATABASE test;
Query OK, 0 rows affected (0.00 sec)

```



修改数据库字符集和校对规则

```shell
mysql> ALTER DATABASE aaaa CHARACTER SET utf8 COLLATE utf8_general_ci;
Query OK, 1 row affected, 2 warnings (0.00 sec)

```



## 数据类型（常用）

### 数值

| 类型         | 大小                                     | 范围（有符号）                                               | 范围（无符号）                                               | 用途                                                 |
| :----------- | :--------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :--------------------------------------------------- |
| TINYINT      | 1 byte                                   | (-128，127)                                                  | (0，255)                                                     | 小整数值                                             |
| INT或INTEGER | 4 bytes                                  | (-2 147 483 648，2 147 483 647)                              | (0，4 294 967 295)                                           | 大整数值                                             |
| BIGINT       | 8 bytes                                  | (-9,223,372,036,854,775,808，9 223 372 036 854 775 807)      | (0，18 446 744 073 709 551 615)                              | 极大整数值                                           |
| DOUBLE       | 8 bytes                                  | (-1.797 693 134 862 315 7 E+308，-2.225 073 858 507 201 4 E-308)，0，(2.225 073 858 507 201 4 E-308，1.797 693 134 862 315 7 E+308) | 0，(2.225 073 858 507 201 4 E-308，1.797 693 134 862 315 7 E+308) | 双精度 浮点数值                                      |
| DECIMAL      | 对DECIMAL(M,D) ，如果M>D，为M+2否则为D+2 | 依赖于M和D的值                                               | 依赖于M和D的值                                               | 小数值，字符串模拟小数，在对数字敏感的业务中使用这个 |



### 日期

| 类型      | 大小 ( bytes) | 范围                                                         | 格式                | 用途                     |
| :-------- | :------------ | :----------------------------------------------------------- | :------------------ | :----------------------- |
| DATE      | 3             | 1000-01-01/9999-12-31                                        | YYYY-MM-DD          | 日期值                   |
| TIME      | 3             | '-838:59:59'/'838:59:59'                                     | HH:MM:SS            | 时间值或持续时间         |
| YEAR      | 1             | 1901/2155                                                    | YYYY                | 年份值                   |
| DATETIME  | 8             | 1000-01-01 00:00:00/9999-12-31 23:59:59                      | YYYY-MM-DD HH:MM:SS | 混合日期和时间值         |
| TIMESTAMP | 4             | 1970-01-01 00:00:00/2038结束时间是第 **2147483647** 秒，北京时间 **2038-1-19 11:14:07**，格林尼治时间 2038年1月19 日 凌晨 03:14:07 | YYYYMMDD HHMMSS     | 混合日期和时间值，时间戳 |



### 字符串

| 类型     | 大小                  | 用途                     |
| :------- | :-------------------- | :----------------------- |
| CHAR     | 0-255 bytes           | 定长字符串               |
| VARCHAR  | 0-65535 bytes         | 变长字符串               |
| TEXT     | 0-65 535 bytes        | 长文本数据               |
| LONGTEXT | 0-4 294 967 295 bytes | 极大文本数据             |
| BLOB     | 0-65 535 bytes        | 二进制形式的长文本数据   |
| LONGBLOB | 0-4 294 967 295 bytes | 二进制形式的极大文本数据 |



## 查询表

查询当前数据库所有表

```shell
mysql>  show tables;
+----------------+
| Tables_in_test |
+----------------+
| department     |
| employee       |
+----------------+
2 rows in set (0.00 sec)

```



查询表的结构

```shell
mysql> desc department
    -> ;
+-------+--------------+------+-----+---------+----------------+
| Field | Type         | Null | Key | Default | Extra          |
+-------+--------------+------+-----+---------+----------------+
| id    | bigint       | NO   | PRI | NULL    | auto_increment |
| name  | char(30)     | YES  |     |         |                |
| intro | varchar(100) | NO   | MUL | NULL    |                |
+-------+--------------+------+-----+---------+----------------+
3 rows in set (0.00 sec)

```



## 创建表

创建数据表

```shell
mysql> CREATE TABLE employee (
    -> id BIGINT AUTO_INCREMENT,
    -> name CHAR(20) NOT NULL,
    -> introduction VARCHAR(255) default "",
    -> gmt_create DATETIME default now(),
    -> gmt_update DATETIME default now(),
    -> is_delete TINYINT default 0,
    -> PRIMARY KEY (id)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.04 sec)

mysql> CREATE TABLE department (
    -> id BIGINT AUTO_INCREMENT,
    -> name CHAR(30) default "" comment 'department name',
    -> intro VARCHAR(100) not null comment 'department incroduction',
    -> PRIMARY KEY (id),
    -> UNIQUE INDEX idx_name (name),
    -> KEY idx_intro (intro)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.04 sec)

```

创建数据表也可以配合`IF NOT EXISTS`来避免创建表报错

`AUTO_INCREMENT`表明这列内容是自增的

`default`设置默认值

`PRIMARY KEY`用来指定这个表的主键，主键必须非空唯一

`UNIQUE INDEX <索引名> (列)`用来限定一列数据不能有重复的

`KEY <索引名> (列)`用来给某一列添加索引，使查找速度更快

`comment`用来给字段加上注释，方便别人理解

最后的`ENGINE`和`DEFAULT CHARSET`用来指定表的字符集 



有时候这样创建可能报错，那是因为字段和系统的关键字重合了，推荐使用吧自定义的字段都用` \``括起来（Tab上面那个），比如

```shell
mysql> CREATE TABLE `employee` (
    -> `id` BIGINT AUTO_INCREMENT,
    -> `name` CHAR(20) NOT NULL,
    -> `introduction` VARCHAR(255) default "",
    -> `gmt_create` DATETIME default now(),
    -> `gmt_update` DATETIME default now(),
    -> `is_delete` TINYINT default 0,
    -> PRIMARY KEY (`id`)
    -> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.04 sec)
```

其他语句（DDL, DML, DQL, DCL）也是可以这样使用



## 删除表

```shell
mysql> DROP TABLE department;
Query OK, 0 rows affected (0.02 sec)

```



## 修改表



### 修改列的类型

```shell
mysql> ALTER TABLE employee MODIFY gmt_create TIMESTAMP;
Query OK, 0 rows affected (0.06 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> ALTER TABLE employee MODIFY gmt_update TIMESTAMP;
Query OK, 0 rows affected (0.07 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



### 修改列名

```shell
mysql> ALTER TABLE employee CHANGE introduction intro VARCHAR(300) NULL;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



### 删除列

```shell
mysql> ALTER TABLE employee DROP wage;
Query OK, 0 rows affected (0.04 sec)
Records: 0  Duplicates: 0  Warnings: 0

```



### 添加列

```shell
mysql> ALTER TABLE employee ADD wage DECIMAL(10, 2) AFTER intro;
Query OK, 0 rows affected (0.03 sec)
Records: 0  Duplicates: 0  Warnings: 0

```

添加一列数据，`DECIMAL(M,N)`表示这是个小数有M位，小数位的位数为N

`AFTER`可以指定在某一列后添加，可以不指定



### 修改表名称

```shell
mysql> ALTER TABLE employee RENAME student;
Query OK, 0 rows affected (0.02 sec)

```



