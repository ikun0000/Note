# Docker安装MySQL

```shell
$ docker pull mysql
$ docker run -d -it --name some-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest
bf73d529766a8d7f12133c69e8f1d5676f5c029c7b79003f3b2d3ec7fc533563
$ docker exec -it some-mysql /bin/bash
$ root@bf73d529766a:/#
```

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

用下面的命令设置

```shell
mysql> set character_set_client = utf8;
mysql> set character_set_server = utf8;
mysql> set character_set_connection = utf8;
mysql> set character_set_database = utf8;
mysql> set character_set_results = utf8;
mysql> set collation_connection = utf8_general_ci;
mysql> set collation_database = utf8_general_ci;
mysql> set collation_server = utf8_general_ci;
```

