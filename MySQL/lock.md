# MySQL锁



## 读/写锁

读锁（共享锁）：针对同一份数据，多个读取操作可以同时进行而不会互相影响

写锁（排它锁）：当前写操作没有完成前，它会阻塞其他写锁和读锁



## 表锁/行锁



### 表锁（偏读）

偏向MyISAM存储引擎，开销小，加索快，无死锁；锁定粒度大，发生锁冲突的概率最高，并发度及低



**查看表的上锁情况：**

```sql
mysql> show open tables;
+--------------------+---------------------------+--------+-------------+
| Database           | Table                     | In_use | Name_locked |
+--------------------+---------------------------+--------+-------------+
| test               | mylock                    |      0 |           0 |
| test               | staffs                    |      0 |           0 |
```

`In_use`字段为0，代表没有锁



**手动添加锁：**

```sql
mysql> lock table mylock read, staffs write;
Query OK, 0 rows affected (0.00 sec)

```

mylock添加了读锁，staffs添加了写锁，再次执行`show open tables`的时候发现mylock和staffs表的`In_use`字段为1



mylock加了写锁之后，自己的session和别的session都可以读取。但是无法对该表进行写操作，包括加锁的session也不行。同时加锁的session也无法读取别的表。别的session如果要对这张加锁的表做写操作，会被阻塞，直到加锁的session释放了锁



mylock加上写锁之后，自己的session可以读取加锁表的数据，其他session的读取操作会被阻塞，直到加锁的session释放锁，自己的session可以修改加锁的表，但是不能读取别的表



**解除所有表的锁：**

```sql
mysql> unlock tables;
Query OK, 0 rows affected (0.00 sec)

```



**总结**

MyISAM在执行查询语句前，会自动给涉及到的表加读锁，在执行增删改操作时，，会自动给涉及的表加写锁

对于MyISAM表的读操作（加读锁），不会阻塞其他进程对同一表的读请求，但会阻塞对同一表的写请求，只有当读锁释放的时候才会执行其他进程的写操作

对于MyISAM表的写操作（加写锁），会阻塞其他进程对同一表的读和写操作，只有当写锁释放的时候，才会执行其他进程的读写操作



**表锁定分析**

```sql
mysql> show status like 'Table_locks%';
+-----------------------+-------+
| Variable_name         | Value |
+-----------------------+-------+
| Table_locks_immediate | 18    |
| Table_locks_waited    | 0     |
+-----------------------+-------+
2 rows in set (0.00 sec)

```

`Table_locks_immediate`：产生表级锁定的次数，可以立即获取锁的查询次数，每立即获取锁值加1

`Table_locks_waited`：出现表级锁定争用而发生的等待的次数



### 行锁（偏写）

偏向InnoDB存储引擎，开销大，加锁慢，会出现死锁；锁定粒度小，发生锁冲突的概率最低，并发度最高



通过`for update`在给某一行上锁	

```sql
mysql> begin ;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from test_innodb_lock where a=8 for update;
+------+------+
| a    | b    |
+------+------+
|    8 | 8000 |
+------+------+
1 row in set (0.00 sec)

mysql> commit;
Query OK, 0 rows affected (0.00 sec)

```



分析行锁

```sql
mysql> show status like 'innodb_row_lock%';
+-------------------------------+-------+
| Variable_name                 | Value |
+-------------------------------+-------+
| Innodb_row_lock_current_waits | 0     |
| Innodb_row_lock_time          | 45671 |
| Innodb_row_lock_time_avg      | 11417 |
| Innodb_row_lock_time_max      | 17671 |
| Innodb_row_lock_waits         | 4     |
+-------------------------------+-------+
5 rows in set (0.00 sec)

```

`Innodb_row_lock_current_waits`：当前正在等待锁的数量

**`Innodb_row_lock_time`：从系统启动到现在锁定总时间长度**

**`Innodb_row_lock_time_avg`：每次等待所花的平均时间**

`Innodb_row_lock_time_max`：从系统启动到现在所花费最长的一次所花的时间

**`Innodb_row_lock_waits`：系统启动后到现在总共等待的次数**



### 优化

* 尽量让所有数据检索都通过索引来完成，避免无索引行锁升级为表锁
* 合理设计索引，尽量缩小锁的范围
* 尽可能较少检索条件，避免间隙锁
* 尽量控制事务大小，减少锁定资源量和时间长度
* 尽可能低级别事务隔离