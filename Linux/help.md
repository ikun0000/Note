# 开发基本命令

#### 归档操作

```
tar cvf archive_name.tar dirname/
```

创建一个新的tar文件

```
tar xvf archive_name.tar
```

解压tar文件

```
tar tvf archive_name.tar
```

查看tar文件

```
gzip test.txt
```

创建一个*.gz的压缩文件

```
gzip -d test.txt.gz
```

解压*.gz文件

```
gzip -l *.gz
```

显示压缩的比率

```
bzip2 test.txt
```

创建*.bz2压缩文件

```
bzip2 -d test.txt.bz2
```

解压*.bz2文件

```
unzip test.zip
```

解压*.zip文件

```
unzip -l jasper.zip
```

查看*.zip文件的内容



#### 文件搜索

```
find -iname "MyProgram.c"
```

查找指定文件名的文件（不区分大小写）

```
find -iname "a.js" -exec md5sum {}
```

对找到的文件执行某个命令

```
find ~ -empty
```

查找home目录下的所有空文件

```
grep -i "the" demo_file
```

在文件中查找字符串（不区分大小写）

```
grep -A 3 -i "example" demo_text
```

输出成功匹配的行，以及该行之后的三行

```
grep -r "ramesh" *
```

在一个目录中递归查询包含指定字符串的文件

```
ls -lh
```

以易读的方式显示文件大小（显示为MB，GB...）

```
ls -ltr
```

以最后修改时间升序列出文件



#### SSH登陆

```
ssh -l biezhi host.example.com
```

登陆远程主机

```
ssh -v -l biezhi host.example.com
```

调试ssh客户端

```
ssh -V
```

显示ssh客户端版本



#### 授权操作

```
chown oracle:dba dbora.sh
```

同时将某个文件的属主改为oracle，属组改为dba

```
chown -R oracle:dba /home/oracle
```

使用`-R`选项对目录和目录下的文件进行递归修改

```
chmod ug+rwx file.txt
```

给指定文件的属主和属组所有权限（包括读、写、执行）

```
chmod g-rwx file.txt
```

删除指定文件的属组的所有权限

```
chmod -R ug+rwx file/
```

修改目录的权限以及递归修改目录下面所有文件和子目录的权限



#### 文本操作

```
sed 's/.$//' filename
```

当你将DOS系统中的文件复制到Unix/Linux后，这个文件每行都会以\r\n结尾，sed可以轻易将其转化为Unix格式的文件，使用\n结尾的文件

```
sed -n '1!G; h; p' filename
```

反转文件内容并输出

```
sed '/./=' thegeekstuff.txt | sed 'N; s/\n/ /'
```

为非空行添加行号

```
sort names.txt
```

以升序对文件内容排序

```
sort -r names.txt
```

以降序对文件内容排序

```
sort -t: -k 3n /etc/passwd | more
```

以第三个字段对/etc/passwd的内容排序

```
tail filename.txt
```

tail命令默认显示文件最后的10行文本

```
tail -n N filename.txt
```

你可以使用`-n`参数指定要显示的行数

```
tail -f log-file
```

你可以使用`-f`选项进行实时查看，这个命令执行后会等待，如果有新行添加到文件尾部，它会继续输出新的行，在查看日志时这个选项会非常有用。你可以通过`^C`（SIGINT）终止命令的执行

```
vim +10 filename.txt
```

打开文件并跳第10行

```
vim +/search-term filename.txt
```

打开文件并跳到第一个匹配的行

```
vim -R /etc/passwd
```

以只读方式打开文件

```
awk '!($0 in array) {array[$0]; print}' temp
```

删除重复行

```
awk -F ':' '$3=$4' /etc/passwd
```

打印/etc/passwd中所有包含同样的uid和gid的行

```
awk '{print $2,$5}' employee.txt
```

打印文件中的指定部分的字段



#### 系统操作

```
df -k -h
```

显示文件系统的磁盘使用情况，默认情况下`df -k`将以字节为单位输出磁盘的使用量

```
passwd
```

`passwd`用于在命令行修改密码，使用这个命令会要求你先输入旧密码，然后输入新密码

```
passwd USERNAME
```

root用户使用这个命令修改其他用户的密码，这个时候不需要输入用户的密码

```
ifconfig -a
```

`ifconfig`用于查看和配置Linux系统的网络接口

```
ifconfig eth0 up|down
```

使用`up`和`down`命令启动或停止某个接口

```
uname -a
```

内核名称、主机名、内核版本号、处理器类型之类的信息

```
top
```

`top`命令会显示当前系统中占用资源最多的一些进程（默认以CPU占用率排序）如果你想改变排序方式，可以在结果列表中点击O（大写字母O）会显示所有可用于排序的列，这个时候你就可以选择你想排序的列

```
ps -ef | more
```

查看当前正在运行的所有进程

```
ps -efH | more
```

以树状结构显示当前正在运行的进程

```
top -u oracle
```

如果只想显示某个特定用户的进程，可以使用`-u`选项

```
free -g
```

如果你想以其他单位输出内存的使用量，需要加一个选项，`-g`为GB，`-m`为MB，`-k`为KB，`-b`为字节

```
free -t
```

如果你想查看所有内存的汇总，请使用`-t`选项，使用这个选项会在输出中加一个汇总行

```
shutdown -h now
```

关闭系统并立即关机

```
shutdown -h +10
```

10分钟后关机

```
shutdown -r now
```

重启

```
shutdown -Fr now
```

重启期间强制进行系统检查



#### 其他操作

```
diff -w name_list.txt name_list_new.txt
```

比较的时候忽略空白行

```
export | grep ORACLE
```

输出跟字符串ORACLE匹配的环境变量

```
export ORACLE_HOME=/u01/app/oracle/product/10.2.0
```

设置全局环境变量

```
date -s "08/22/2017 23:59:53"
```

设置系统日期

```
hwclock --systohc -utc
```

当你修改了系统时间，需要同步硬件时间和系统时间

```
wget http://example.com/python3.5.2.tar.gz
```

使用`wget`从网上下载软件、音乐、视频

```
wget -O a.tar.gz http://example.com/python3.5.2.tar.gz
```

下载文件并以指定的文件名保存文件





# 暴龙秘籍

[暴龙秘籍](https://docs-old.fghrsh.net/bdsc_centos.html?page_id=1)

