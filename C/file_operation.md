# 文件操作

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



