# 备份和还原



## Database 2 SQL Script

```shell
root@bf73d529766a:/$ mysqldump -u root -p library > library.sql
```

mysqldump用来把数据库导出成sql文件，`-u`指定用户名，`-p`参数指定密码（上面这条命令会要求你以交互式方式在终端输入指定用户名的密码），备份的数据库为library，导出到当前目录的library.sql文件



## SQL Script 2 Database

### 方式一

Step 1:

```shell
$ mysql -u root -p123456
```

登陆数据库

Step 2:

```sql
mysql> CREATE DATABASE library;
mysql> use library;
```

创建（同名，不是一定）数据库并选择

Step 3:

```sql
mysql> source library.sql;
```

执行SQL脚本（如果不在当前目录则写绝对路径）



### 方式二

Step 1:

```shell
$ mysql -u root -p123456
```

先登录数据库

Step 2:

```sql
mysql> CREATE DATABASE library;
```

创建好数据库

Step 3:

```shell
root@bf73d529766a:/$ mysql -u root -p library < library.sql 
Enter password: 
```

在终端中使用mysql命令导入library.sql