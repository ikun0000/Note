# 文件操作

### 重要的事情说三遍

***写完open立刻写close***

***写完open立刻写close***

***写完open立刻写close***

***写完opendir立刻写closedir***

***写完opendir立刻写closedir***

***写完opendir立刻写closedir***



### 文件描述符

就是一个数字，每个数字代表一个打开的文件

Linux默认打开一个终端就会打开3个文件描述符STDIN_FILENO（0）、STDOUT_FILENO（1）、STDERR_FILENO（2）

```c
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <fcntl.h>

int main()
{
    int fd;

    fd = open("test.txt", O_CREAT | O_EXCL | O_WRONLY, S_IRUSR | S_IWUSR | S_IRGRP);

    if (fd < 0)
    {
        fprintf(stderr, "open(2) error\n");
        return -1;
    }
    dup2(fd, fileno(stdout));
    printf("aaaaaaaaaaaaaaaaaaaaaa\n");
	
    close(fd);
    return 0;
}
```

例子使用`dup2(2)`来指定把fd复制到**标准输出**的文件描述符，此后**标准输出**就指向fd打开的文件，对`stdout`的操作实际上都是对fd操作（[dup(2)](http://man7.org/linux/man-pages/man2/dup.2.html)）

`dup(2)`是有系统自动分配最小可用描述符，而`dup2(2)`则是自己指定描述符



### 文件创建/读写

弱化版`cp(1)`

```c
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>

#define BLKSIZE     1024

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("usage: a.out <src> <dst>\n");
        return 0;
    }

    char block[S_BLKSIZE];
    int infd, outfd;
    int nread;

    infd = open(argv[1], O_RDONLY);		// 以只读方式打开源文件
    outfd = open(argv[2], O_WRONLY | O_CREAT | O_TRUNC | O_EXCL, S_IRUSR | S_IWUSR | S_IRGRP);			// 以只写方式创建目标文件并截短为0，并指定文件权限
    if (infd < 0 || outfd < 0)
    {
        fprintf(stderr, "open(2) error\n");
        return -1;
    }

    while ((nread = read(infd, block, BLKSIZE)) > 0)		// 复制内容
    {
        write(outfd, block, nread);
    }
	
    close(infd);
    close(outfd);
    return 0;
}
```

open(2)可以提供第三个参数指定文件的权限，要查看`flags`和`mode`可以通过`man open`（[open(2)]( http://man7.org/linux/man-pages/man2/open.2.html )）去查看

open(2)成功返回非负整数，失败返回负数并设置`errno`

先通过`read(2)`读取指定文件描述符的内容（[read(2)]( http://man7.org/linux/man-pages/man2/read.2.html )）

之后通过`write(2)`向指定文件描述符输出（[write(2)]( http://man7.org/linux/man-pages/man2/write.2.html )）

close(2)关闭一个文件描述符（[close(2)]( http://man7.org/linux/man-pages/man2/close.2.html )）



常用的`flags`和`mode`组合

```c
int fd;
fd = open("test.txt", O_CREAT | O_EXCL | O_TRUNC | O_WRONLY, S_IRUSR | S_IWUSR | S_IRGRP);       // 创建一个以只写方式打开的文件，如果当前目录有同名文件则创建失败，成功把文件截短为0，并赋予文件权限-rw-r-----
fd = open("test.txt", O_WRONLY | O_APPEND);     // 以只写方式打开文件，并设置文件指针为文件末尾
fd = open("test.txt", O_WRONLY | O_TRUNC);		// 以只写方式打开文件，并设置文件指针为0
fd = open("test.txt", O_RDONLY);		// 只读方式打开文件
fd = open("test.txt", O_RDWR);			// 读写方式打开文件
```



### 文件偏移

```c
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>

int main(int argc, char *argv[])
{
    int fd;
    char block[10];
    int nread;

    fd = open("test.txt", O_RDONLY);
    if (fd < 0)
    {
        fprintf(stderr, "open(2) error\n");
        return -1;
    }

    if (lseek(fd, 5, SEEK_SET) < 0)			// 相对文件头偏移5
    {
        fprintf(stderr, "lseek(2) error\n");
        return -2;
    }

    if ((nread = read(fd, block, 10)) > 0)
    {
        write(STDOUT_FILENO, block, nread);
    }
	
    close(fd);
    return 0;
}
```

lseek(2)会改变文件指针的指向，可以相对于不同位置进行偏移（[lseek(2)]( http://man7.org/linux/man-pages/man2/lseek.2.html )）



### 获取文件信息

超级弱化版的`stat(1)`

```c
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <pwd.h>
#include <grp.h>
#include <sys/times.h>
#include <time.h>
#include <sys/sysmacros.h>


void gettype(mode_t mode, char *str, int size)		// 获取文件类型
{
    if (S_ISDIR(mode)) strncpy(str, "directory", size); 
    else if (S_ISREG(mode)) strncpy(str, "regular file", size);
    else if (S_ISBLK(mode)) strncpy(str, "block file", size);
    else if (S_ISFIFO(mode)) strncpy(str, "FIFO file", size);
    else if (S_ISCHR(mode)) strncpy(str, "character", size);
    else if (S_ISSOCK(mode)) strncpy(str, "socket file", size);
}


void getaccess(mode_t mode, char *str, int size)		// 获取问价访问权限的字符表示
{
    memset(str, '-', 10);
    if (mode & S_IFREG) str[0] = '-';
    else if (mode & S_IFDIR) str[0] = 'd';
    else if (mode & S_IFCHR) str[0] = 'c';
    else if (mode & S_IFBLK) str[0] = 'b';
    else if (mode & S_IFSOCK) str[0] = 's';
    else if (mode & S_IFIFO) str[0] = 'p';
    
    if (mode & S_IRUSR) str[1] = 'r';
    if (mode & S_IWUSR) str[2] = 'w';
    if (mode & S_IXUSR) str[3] = 'x';
    if (mode & S_IRGRP) str[4] = 'r';
    if (mode & S_IWGRP) str[5] = 'w';
    if (mode & S_IXGRP) str[6] = 'x';
    if (mode & S_IROTH) str[7] = 'r';
    if (mode & S_IWOTH) str[8] = 'w';
    if (mode & S_IXOTH) str[9] = 'x';
    if (mode & S_ISUID) str[3] = 's';
    if (mode & S_ISGID) str[6] = 's';
    if (mode & S_ISVTX) str[9] = 't';
    str[10] = 0;
}

int getaccessnum(mode_t mode)				// 获取文件访问权限的数字表示
{
    int n = 0;
    if (mode & S_IRUSR) n |= S_IRUSR;
    if (mode & S_IWUSR) n |= S_IWUSR;
    if (mode & S_IXUSR) n |= S_IXUSR;
    if (mode & S_IRGRP) n |= S_IRGRP;
    if (mode & S_IWGRP) n |= S_IWGRP;
    if (mode & S_IXGRP) n |= S_IXGRP;
    if (mode & S_IROTH) n |= S_IROTH;
    if (mode & S_IWOTH) n |= S_IWOTH;
    if (mode & S_IXOTH) n |= S_IXOTH;
    if (mode & S_ISUID) n |= S_ISUID;
    if (mode & S_ISGID) n |= S_ISGID;
    if (mode & S_ISVTX) n |= S_ISVTX;
    return n;
}


int main(int argc, char *argv[])
{
    int fd;
    struct stat statbuf;
    char strbuf[100];
    struct passwd *passinfo;
    struct group *grpinfo;
    struct tm *tmbuf;

    if (argc != 2)
    {
        printf("usage: a.out <filepath>\n");
        return 0;
    }

    if (lstat(argv[1], &statbuf) < 0)
    {
        fprintf(stderr, "lstat(2) error\n");
        return -1;
    }

    printf("  File: %s\n", argv[1]);
    gettype(statbuf.st_mode, strbuf, 100);
    printf("  Size: %d\t\t\tBlock: %d\t\tIO Block: %d\t%s\n", 
           statbuf.st_size, statbuf.st_blocks, statbuf.st_blksize, strbuf);
    printf("Device: %d0%dh/%dd\t\tInode: %d\t\tLinks: %d\n", 
            major(statbuf.st_dev), minor(statbuf.st_dev), statbuf.st_dev, statbuf.st_ino, statbuf.st_nlink);
    getaccess(statbuf.st_mode, strbuf, 100);
    passinfo = getpwuid(statbuf.st_uid);
    grpinfo = getgrgid(statbuf.st_gid);
    printf("Access: (0%o/%s)\tUid: (\t%d/\t%s)\tGid: (\t%d/\t%s)\n", 
           getaccessnum(statbuf.st_mode), strbuf, statbuf.st_gid, passinfo->pw_name, 
           statbuf.st_gid, grpinfo->gr_name);

    tmbuf = gmtime(&statbuf.st_atime);
    strftime(strbuf, 100, "%F %T.%s %Z", tmbuf);
    printf("Access: %s\n", strbuf);
    tmbuf = gmtime(&statbuf.st_mtime);
    strftime(strbuf, 100, "%F %T.%s %Z", tmbuf);
    printf("Modify: %s\n", strbuf);
    tmbuf = gmtime(&statbuf.st_ctime);
    strftime(strbuf, 100, "%F %T.%s %Z", tmbuf);
    printf("Change: %s\n", strbuf);

    return 0;
}
```

使用`stat(2)`或`lstat(2)`获取文件信息（[stat(2)]( http://man7.org/linux/man-pages/man2/stat.2.html )、[lstat(2)]( http://man7.org/linux/man-pages/man2/lstat.2.html )）

还有一个是`fstat(2)`，一般以f开头的函数代表传入的参数是**文件描述符**而不是**文件名**

`lstat(2)`和`stat(2)`在传入的文件不是**符号链接**的时候的执行效果是一样的，当传入的文件名是**符号链接**时，`stat(2)`会返回**符号链接**指向的文件的文件信息，而`lstat(2)`返回的则是符号链接本身的信息

顺便在说说时间问题，C语言有标准库处理时间（[time(2)]( http://man7.org/linux/man-pages/man2/time.2.html )、[difftime(3)]( http://man7.org/linux/man-pages/man3/difftime.3.html )、[gmtime(3)]( http://man7.org/linux/man-pages/man3/gmtime.3p.html )、[localtime(3)]( http://man7.org/linux/man-pages/man3/localtime.3p.html )、[mktime(3)]( http://man7.org/linux/man-pages/man3/mktime.3p.html )、[ctime(3)]( http://man7.org/linux/man-pages/man3/mktime.3.html )、[asctime(3)]( http://man7.org/linux/man-pages/man3/asctime.3p.html )、[strftime(3)]( http://man7.org/linux/man-pages/man3/strftime.3.html )、[strptime(3)]( http://man7.org/linux/man-pages/man3/strptime.3.html )）



### 其他文件和目录操作

1. 修改文件的属主和属组

   [chown(2)](http://man7.org/linux/man-pages/man2/chown.2.html)

2. 修改文件的属主和属组

   [chmod(2)](http://man7.org/linux/man-pages/man2/chmod.2.html)

3. 创建/删除文件链接

   [link(2)](http://man7.org/linux/man-pages/man2/link.2.html)、[symlink(2)](http://man7.org/linux/man-pages/man2/symlink.2.html)、[unlink(2)](http://man7.org/linux/man-pages/man2/unlink.2.html)

   `link(2)`用于创建一个硬链接，之后修改硬链接不会影响到原来文件，每创建一次硬链接`stat->st_nlink`就会增加1

   `symlink(2)`会创建一个符号链接，符号链接指向源文件，符号链接的大小就是源文件文件名的长度

   `unlink(2)`删除一个文件链接，在删除一个文件时`stat->st_nlink`会减1，要真正删除文件在磁盘的记录必须是要`stat->st_nlink`为0

4. 创建/删除目录

   [mkdir(3)](http://man7.org/linux/man-pages/man3/mkdir.3p.html)

   [rmdir(3)](https://linux.die.net/man/3/rmdir)

5. 获取/切换目录

   [getcwd(2)](http://man7.org/linux/man-pages/man2/getcwd.2.html)

   [chdir(3)](http://man7.org/linux/man-pages/man3/chdir.3p.html)

   

### 目录扫描

弱化版`ls(1)`

```c
#include <unistd.h>
#include <dirent.h>
#include <fcntl.h>
#include <sys/types.h>
#include <stdio.h>

int main(int argc, char *argv[])
{
    DIR *dir;
    struct dirent *entry;

    if (argc != 2)
    {
        printf("usage: a.out <dirpath>\n");
        return 0;
    }
    
    if ((dir = opendir(argv[1])) == NULL)
    {
        fprintf(stderr, "opendir(3) error\n");
        return -1;
    }
   
    printf("inode\tname\n");
    while ((entry = readdir(dir)) != NULL)
    {
        printf("%ld\t%s\n", entry->d_ino, entry->d_name);
    }

    closedir(dir);
    return 0;
}
```

首先`opendir(3)`打开目录，然后`readdir(3)`遍历目录，返回一个`struct dirent`结构，最后关闭目录（[opendir(3)](http://man7.org/linux/man-pages/man3/readdir.3.html)、[readdir(3)](http://man7.org/linux/man-pages/man3/readdir.3.html)、[closedir(3)](http://man7.org/linux/man-pages/man3/closedir.3.html)）

​	

### 感觉用的不多的文件操作

1. 文件同步

   [sync(2)](http://man7.org/linux/man-pages/man2/sync.2.html)、[fsync(2)](http://man7.org/linux/man-pages/man2/fdatasync.2.html)

2. 文件截断

   [truncate(3)](http://man7.org/linux/man-pages/man3/truncate.3p.html)、[ftruncate(3)](http://man7.org/linux/man-pages/man3/ftruncate.3p.html)
   
3. 获取/移动当前目录指针

   [telldir(3)](http://man7.org/linux/man-pages/man3/telldir.3.html)、[seekdir(3)](http://man7.org/linux/man-pages/man3/seekdir.3.html)



### 标准IO

C语言提供的更高级的IO操作

参考：[菜鸟教程 C 标准库 - <stdio.h>](https://www.runoob.com/cprogramming/c-standard-library-stdio-h.html)



