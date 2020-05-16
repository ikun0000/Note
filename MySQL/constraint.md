# MySQL约束



## 主键约束

当设置一列为主键约束之后那么这一列的数据不能为空，不能重复。主键只能设置一个

创建主键约束有两种方式

```sql
CREATE TABLE user (
	id BIGINT PRIMARY KEY,
    name CHAR(3O)
);
```

```sql
CREATE TABLE user (
	id BIGINT,
    name CHAR(3O),
    PRIMARY KEY (id)
);
```

如果需要在已有的表添加主键约束使用

```sql
ALTER TABLE user ADD PRIMARY KEY (id);
```

删除主键约束

```sql
ALTER TABLE user DROP PRIMARY KEY;
```



自增长

```sql
CREATE TABLE user (
	id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    name CHAR(3O)
);
```



## 非空约束

```sql
CREATE TABLE user (
	id BIGINT,
    name CHAR(3O) NOT NULL
);
```

加上之后name不能为空，如果需要设置默认值如下

```sql
CREATE TABLE user (
	id BIGINT,
    name CHAR(3O) NOT NULL DEFAULT "default value"
);
```



## 唯一约束

```sql
CREATE TABLE user (
	id BIGINT,
    name CHAR(3O) UNIQUE
);
```

这时name只能是唯一的

还可以使用下面的方式创建唯一约束

```sql
CREATE TABLE user (  
    id BIGINT, 
    name CHAR(30), 
    UNIQUE INDEX idx_name (name) 
);
```

`idx_name`为唯一约束名，可以任意命名



## 外键约束

一个表的一列引用了另外一张表（本表）的主键

在插入数据时外键约束列的数据值必须存在于被引用的目标列中

```sql
CREATE TABLE dept ( 
    id BIGINT PRIMARY KEY, 
    name CHAR(30) NOT NULL 
);

CREATE TABLE emp ( 
    id BIGINT PRIMARY KEY, 
    name CHAR(30) NOT NULL, 
    dept_id BIGINT, 
    CONSTRAINT fk_emp_dept FOREIGN KEY(dept_id) REFERENCES dept(id) 
);
```

`fk_emp_dept`是外键名

在已有的表添加外键约束

```sql
ALTER TABLE emp ADD CONSTRAINT fk_emp_dept FOREIGN KEY(dept_id) REFERENCES dept(id);
```