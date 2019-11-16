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



# 暴龙秘籍

[暴龙秘籍](https://docs-old.fghrsh.net/bdsc_centos.html?page_id=1)

