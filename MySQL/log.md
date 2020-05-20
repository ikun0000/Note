# 日志


## 慢查询日志

```sql
mysql> SHOW VARIABLES LIKE '%slow_query_log%'
    -> ;
+---------------------+--------------------------------------+
| Variable_name       | Value                                |
+---------------------+--------------------------------------+
| slow_query_log      | OFF                                  |
| slow_query_log_file | /var/lib/mysql/cf4f7b16765c-slow.log |
+---------------------+--------------------------------------+
2 rows in set (0.01 sec)

```

默认是关闭慢查询日志，不建议一直开启，有需要再开启

开启慢查询日志（只对当前数据库有用）

```sql
mysql> set global slow_query_log=1;
Query OK, 0 rows affected (0.01 sec)

```



默认情况下查询时间***大于***10秒为慢查询，就会被记录

```sql
mysql> SHOW VARIABLES LIKE '%long_query_time%';
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)

```

设置慢查询时间

```sql
mysql> set global long_query_time=3;
Query OK, 0 rows affected (0.00 sec)

mysql> SHOW VARIABLES LIKE '%long_query_time%';
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)

```

设置之后还是10，需要新开一个窗口连接



查看有几条慢查询，可以作为系统健康的指标

```sql
mysql> show global status like 'Slow_queries';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Slow_queries  | 1     |
+---------------+-------+
1 row in set (0.00 sec)

```



### 慢查询日志分析工具

得到返回记录集最多的10个SQL

```shell
$ mysqldumpslow -s -r -t 10 /var/lib/mysql/cf4f7b16765c-slow.log
```



得到访问次数最多的10个SQL

```shell
$ mysqldumpslow -s -c -t 10 /var/lib/mysql/cf4f7b16765c-slow.log
```



得到按照时间排序的的前10条包含左连接查询的语句

```shell
$ mysqldumpslow -s t -t 10 -g "left join" /var/lib/mysql/cf4f7b16765c-slow.log
```



使用时可以结合more或者less使用

```shell
$ mysqldumpslow -s -r -t 10 /var/lib/mysql/cf4f7b16765c-slow.log | less
```



## Show profile

profile log默认是关闭的

```sql
mysql> show variables like 'profiling';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| profiling     | OFF   |
+---------------+-------+
1 row in set (0.00 sec)

```

开启profile log

```sql
set global profiling=ON
```

然后从新连接



之后执行SQL，要查看执行的SQL和耗时使用下面命令

```sql
mysql> show profiles;
+----------+------------+----------------------------------+
| Query_ID | Duration   | Query                            |
+----------+------------+----------------------------------+
|        1 | 0.00022800 | select @@version_comment limit 1 |
|        2 | 0.00134375 | show variables like 'profiling'  |
|        3 | 0.00021725 | SELECT DATABASE()                |
|        4 | 0.00098375 | show databases                   |
|        5 | 0.00109350 | show tables                      |
|        6 | 0.00065825 | select * from question           |
+----------+------------+----------------------------------+
6 rows in set, 1 warning (0.00 sec)

```

查询一条语句的完整生命周期每一步的信息（cpu使用信息和io信息）

```sql
mysql> show profile cpu,block io for query 2;
+----------------------+----------+----------+------------+--------------+---------------+
| Status               | Duration | CPU_user | CPU_system | Block_ops_in | Block_ops_out |
+----------------------+----------+----------+------------+--------------+---------------+
| starting             | 0.000096 | 0.000023 |   0.000068 |            0 |             0 |
| checking permissions | 0.000027 | 0.000006 |   0.000019 |            0 |             0 |
| Opening tables       | 0.000110 | 0.000028 |   0.000083 |            0 |             0 |
| init                 | 0.000074 | 0.000018 |   0.000055 |            0 |             0 |
| System lock          | 0.000017 | 0.000004 |   0.000012 |            0 |             0 |
| optimizing           | 0.000011 | 0.000003 |   0.000007 |            0 |             0 |
| optimizing           | 0.000009 | 0.000002 |   0.000007 |            0 |             0 |
| statistics           | 0.000022 | 0.000006 |   0.000017 |            0 |             0 |
| preparing            | 0.000029 | 0.000007 |   0.000020 |            0 |             0 |
| statistics           | 0.000018 | 0.000004 |   0.000014 |            0 |             0 |
| preparing            | 0.000016 | 0.000004 |   0.000012 |            0 |             0 |
| executing            | 0.000828 | 0.000832 |   0.000000 |            0 |             0 |
| end                  | 0.000016 | 0.000011 |   0.000000 |            0 |             0 |
| query end            | 0.000010 | 0.000010 |   0.000000 |            0 |             0 |
| removing tmp table   | 0.000014 | 0.000014 |   0.000000 |            0 |             0 |
| query end            | 0.000006 | 0.000006 |   0.000000 |            0 |             0 |
| closing tables       | 0.000010 | 0.000010 |   0.000000 |            0 |             0 |
| freeing items        | 0.000021 | 0.000021 |   0.000000 |            0 |             0 |
| cleaning up          | 0.000012 | 0.000012 |   0.000000 |            0 |             0 |
+----------------------+----------+----------+------------+--------------+---------------+
19 rows in set, 1 warning (0.00 sec)

```

如果在`show profile`出现以下的东西说明SQL有问题

* converting HEAP to MyISAM：查询结果太大，内存不够用，往磁盘写数据了
* Creating tmp table：创建临时表，拷贝数据到临时表，用完再删除
* Copying to tmp table on disk：把内存中的临时表复制到磁盘，***危险！！！***
* locked：



## 全局查询日志

***永远不要在生产环境开启这个功能***

开启全局日志

```sql
set global general_log=1;
set global log_output='TABLE';
```



查看执行的语句

```sql
select * from mysql.general_log;
```

