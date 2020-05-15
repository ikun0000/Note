# DCL(Data Control Language)语句



## 创建用户

格式

```sql
CREATE USER <username>@<ip address> IDENTIFIED BY '<password>';
```



指定创建用户的登陆地址

```shell
mysql> CREATE USER user1@10.10.10.22 IDENTIFIED BY 'tool';
Query OK, 0 rows affected (0.01 sec)

```



创建所有地址都可以登陆的用户

```shell
mysql> CREATE USER user2@'%' IDENTIFIED BY 'tool';
Query OK, 0 rows affected (0.01 sec)

```



`mysql.user`表保存着MySQL的用户

```shell
mysql> SELECT User,Host FROM mysql.user;
+------------------+-------------+
| User             | Host        |
+------------------+-------------+
| root             | %           |
| user2            | %           |
| user1            | 10.10.10.22 |
| mysql.infoschema | localhost   |
| mysql.session    | localhost   |
| mysql.sys        | localhost   |
| root             | localhost   |
+------------------+-------------+
7 rows in set (0.00 sec)

```





## 用户授权

格式

```sql
GRANT <Authority1>,[<Authority2>] ON <database>.<table> TO <username>@<ip address>;
```



```shell
mysql> GRANT CREATE,ALTER,DROP,INSERT,UPDATE,DELETE,SELECT ON test.* TO user1@10.10.10.22;
Query OK, 0 rows affected (0.00 sec)

mysql> GRANT ALL ON test.* TO user2@'%';
Query OK, 0 rows affected (0.00 sec)

```

上面给user1@10.10.10.22这个用户授予`CREATE`,`ALTER`,`DROP`,`INSERT`,`UPDATE`,`DELETE`,`SELECT`这些权限，并且这些权限只能在test数据库中所有表中才有效

使用`ALL`即设置全部权限



## 撤销授权

格式

```sql
REVOKE <Authority1>,[<Authority2>] ON <database>.<table> FROM <username>@<ip address>;
```



```shell
mysql> REVOKE UPDATE,DELETE,CREATE,DROP ON test.* FROM user1@10.10.10.22;
Query OK, 0 rows affected (0.00 sec)

mysql> REVOKE ALL ON test.* FROM user1@10.10.10.22;
Query OK, 0 rows affected (0.00 sec)

```

取消用户user1@10.10.10.22的`UPDATE`,`DELETE`,`CREATE`,`DROP`权限



## 查看权限

```shell
mysql> SHOW GRANTS FOR user1@10.10.10.22;
+------------------------------------------------------------------------------------------------+
| Grants for user1@10.10.10.22                                                                   |
+------------------------------------------------------------------------------------------------+
| GRANT USAGE ON *.* TO `user1`@`10.10.10.22`                                                    |
| GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER ON `test`.* TO `user1`@`10.10.10.22` |
+------------------------------------------------------------------------------------------------+
2 rows in set (0.00 sec)

```



## 修改用户密码

```shell
mysql> ALTER USER root@'%' IDENTIFIED WITH mysql_native_password BY '123456';
Query OK, 0 rows affected (0.00 sec)

```





## 删除用户

```shell
mysql> DROP USER user2@'%'
    -> ;
Query OK, 0 rows affected (0.01 sec)

```



