# Docker安装MySQL

```shell
$ docker pull mysql
$ docker run -d -it --name some-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest
bf73d529766a8d7f12133c69e8f1d5676f5c029c7b79003f3b2d3ec7fc533563
$ docker exec -it some-mysql /bin/bash
$ root@bf73d529766a:/#
```

在使用Docker安装MySQL可以通过`-e`参数指定MySQL的一些环境，比如上面的`MYSQL_ROOT_PASSWORD`指定了root密码，可以配置项如下：

| 环境变量                       | 功能                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| `MYSQL_DATABASE`               | 此变量是可选的，允许指定启动时要创建的数据库的名称。如果提供了用户/密码，则将授予该用户对该数据库的超级用户访问权限（对应于GRANT ALL）。 |
| `MYSQL_USER`, `MYSQL_PASSWORD` | 这些变量是可选的，与创建新用户和设置该用户的密码一起使用。将为该用户授予MYSQL_DATABASE变量指定的数据库的超级用户权限。这两个变量都是创建用户所必需的。 |
| `MYSQL_ALLOW_EMPTY_PASSWORD`   | 这是一个可选变量。设置为yes允许容器以root用户的空白密码启动。 |

如果用yum安装可以用mariadb代替

```shell
$ yum -y install MariaDB-server MariaDB-client

$ systemctl start mariadb #启动服务
$ systemctl enable mariadb #设置开机启动
 
$ systemctl restart mariadb #重新启动
$ systemctl stop mariadb.service #停止MariaDB

$ mysql_secure_installation 		# 初始化mysql
```



# 登陆MySQL

```shell
$ mysql -u root -p<密码，中间没有空格>
```



查看MySQL数据文件位置

```shell
mysql> show variables like '%dir%'
    -> ;
+-----------------------------------------+--------------------------------+
| Variable_name                           | Value                          |
+-----------------------------------------+--------------------------------+
| basedir                                 | /usr/                          |
| binlog_direct_non_transactional_updates | OFF                            |
| character_sets_dir                      | /usr/share/mysql-8.0/charsets/ |
| datadir                                 | /var/lib/mysql/                |
| innodb_data_home_dir                    |                                |
| innodb_directories                      |                                |
| innodb_doublewrite_dir                  |                                |
| innodb_log_group_home_dir               | ./                             |
| innodb_max_dirty_pages_pct              | 90.000000                      |
| innodb_max_dirty_pages_pct_lwm          | 10.000000                      |
| innodb_redo_log_archive_dirs            |                                |
| innodb_temp_tablespaces_dir             | ./#innodb_temp/                |
| innodb_tmpdir                           |                                |
| innodb_undo_directory                   | ./                             |
| lc_messages_dir                         | /usr/share/mysql-8.0/          |
| plugin_dir                              | /usr/lib/mysql/plugin/         |
| slave_load_tmpdir                       | /tmp                           |
| tmpdir                                  | /tmp                           |
+-----------------------------------------+--------------------------------+
18 rows in set (0.02 sec)
```

可以看到`datadir`（数据文件）的目录存放在`/var/lib/mysql/  `



# 修改MySQL编码为UTF-8

```shell
mysql> show variables like '%char%';
+--------------------------+--------------------------------+
| Variable_name            | Value                          |
+--------------------------+--------------------------------+
| character_set_client     | latin1                         |
| character_set_connection | latin1                         |
| character_set_database   | utf8mb4                        |
| character_set_filesystem | binary                         |
| character_set_results    | latin1                         |
| character_set_server     | utf8mb4                        |
| character_set_system     | utf8                           |
| character_sets_dir       | /usr/share/mysql-8.0/charsets/ |
+--------------------------+--------------------------------+
8 rows in set (0.01 sec)

```

用下面的命令设置（下面的只是临时生效的，只在当前窗口）

```shell
# 设置客户端连接未utf8
mysql> set character_set_client = utf8;
# 设置服务器编码为utf8
mysql> set character_set_server = utf8;
# 设置连接默认编码为utf8
mysql> set character_set_connection = utf8;
# 设置创建数据库默认编码为utf8
mysql> set character_set_database = utf8;
# 设置返回客户端的信息为utf8
mysql> set character_set_results = utf8;
# 设置默认数据库的collate为utf8_general_ci
mysql> set collation_connection = utf8_general_ci;
mysql> set collation_database = utf8_general_ci;
mysql> set collation_server = utf8_general_ci;
```

可以不用全部修改，只改`character_set_client`和`character_set_results`就可以，只要他们是一样的就行

Docker安装时带上下面参数设置编码为utf8

```
--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```



# 查看数据库支持的引擎

```sql
mysql> show engines;
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
| Engine             | Support | Comment                                                        | Transactions | XA   | Savepoints |
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
| FEDERATED          | NO      | Federated MySQL storage engine                                 | NULL         | NULL | NULL       |
| MEMORY             | YES     | Hash based, stored in memory, useful for temporary tables      | NO           | NO   | NO         |
| InnoDB             | DEFAULT | Supports transactions, row-level locking, and foreign keys     | YES          | YES  | YES        |
| PERFORMANCE_SCHEMA | YES     | Performance Schema                                             | NO           | NO   | NO         |
| MyISAM             | YES     | MyISAM storage engine                                          | NO           | NO   | NO         |
| MRG_MYISAM         | YES     | Collection of identical MyISAM tables                          | NO           | NO   | NO         |
| BLACKHOLE          | YES     | /dev/null storage engine (anything you write to it disappears) | NO           | NO   | NO         |
| CSV                | YES     | CSV storage engine                                             | NO           | NO   | NO         |
| ARCHIVE            | YES     | Archive storage engine                                         | NO           | NO   | NO         |
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
9 rows in set (0.00 sec)

```



# 查看默认引擎

```sql
mysql> show variables like '%storage_engine%';
+---------------------------------+-----------+
| Variable_name                   | Value     |
+---------------------------------+-----------+
| default_storage_engine          | InnoDB    |
| default_tmp_storage_engine      | InnoDB    |
| disabled_storage_engines        |           |
| internal_tmp_mem_storage_engine | TempTable |
+---------------------------------+-----------+
4 rows in set (0.00 sec)

```



# 两种主要引擎对比

| 对比项   | MyISAM                                                 | InnoDB                                                       |
| -------- | ------------------------------------------------------ | ------------------------------------------------------------ |
| 主外键   | 不支持                                                 | 支持                                                         |
| 事务     | 不支持                                                 | 支持                                                         |
| 行表锁   | 表锁，即使操作一行记录也会锁住整个表，不适合高并发操作 | 行锁，操作时只锁某一行，不对其他行有影响，适合高并发操作     |
| 缓存     | 只缓存索引，不缓存真实数据                             | 不仅缓存索引还要缓存真实数据，对内存要求较高，而且内存大小对性能有决定性影响 |
| 表空间   | 小                                                     | 大                                                           |
| 关注点   | 性能                                                   | 事务                                                         |
| 默认安装 | Y                                                      | Y                                                            |

