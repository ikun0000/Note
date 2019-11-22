# 管道

```c
#include <stdio.h>
#include <string.h>

int main()
{
    FILE *fp;
    char buffer[1024];
    memset(buffer, 0, 1024);    

    if ((fp = popen("openssl md5 --help", "r")) == NULL)
    {
        fprintf(stderr, "fopen(3) error\n");
        return 1;
    }
    printf("# openssl md5 --help\n");
    fgets(buffer, 1024, fp);
    printf("%s\n", buffer);
    pclose(fp);

    if ((fp = popen("openssl md5", "w")) == NULL)
    {
        fprintf(stderr, "fopen(3) error\n");
        return 1;
    }
    fputs("123456", fp);
    pclose(fp);

    return 0;
}
```

`popen(3)`和`pclose(3)`用来创建管道和关闭管道（[popen(3)](http://man7.org/linux/man-pages/man3/popen.3.html)、[pclose(3)](http://man7.org/linux/man-pages/man3/pclose.3p.html)）



### `pipe(2)`调用

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>

int main()
{
    pid_t pid;
    int pipefd1[2];
    int pipefd2[2];
    char buffer[30] = {0, };

    if (pipe(pipefd1) != 0 || pipe(pipefd2) != 0)
    {
        fprintf(stderr, "pipe(2) error\n");
        exit(1);
    }
    
    if ((pid = fork()) < 0)
    {
        fprintf(stderr, "fork(2) error\n");
        exit(2);
    }
    else if (pid == 0)
    {
        close(pipefd1[0]);
        close(pipefd2[1]);
        
        read(pipefd2[0], buffer, 30);
        printf("get requests: %s\n", buffer);
        write(pipefd1[1], "HTTP/1.1 200 OK", 15);

        exit(0);
    }
    
    close(pipefd1[1]);
    close(pipefd2[0]);
    
    write(pipefd2[1], "GET / HTTP/1.1", 14);
    read(pipefd1[0], buffer, 30);
    printf("get response: %s\n", buffer);

    waitpid(pid, NULL, 0);
    return 0;
}
```

`pipe(2)`则是打开两个文件描述符，`pipefd[0]`用来读取数据，`pipefd[1]`用来写入数据。通过使用两对`pipefd`实现父子进程的数据交互，父进程分别关闭一组的读和另一组的写，子进程则关闭相反的读写来实现父子进程的管道（[pipe(2)](http://man7.org/linux/man-pages/man2/pipe.2.html)）



### FIFO文件

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
int main()
{
    pid_t pid;
    int fd;
    char buffer[10] = {0, };

    if (mkfifo("fifo.file", S_IRUSR | S_IWUSR | S_IRGRP) != 0)
    {
        fprintf(stderr, "mkfifo(3) error\n");
        exit(-1);
    }
    
    if ((pid = fork()) < 0)
    {
        fprintf(stderr, "fork(2) error\n");
        exit(1);
    }
    else if (pid == 0)
    {
        fd = open("fifo.file", O_WRONLY);
        dup2(fd, STDOUT_FILENO);
        write(STDOUT_FILENO, "Hello!", 6);
        close(fd);
        exit(0);
    }

    fd = open("fifo.file", O_RDONLY);
    read(fd, buffer, 10);
    printf("receive: %s\n", buffer);
    close(fd);
    return 0;
}
```

这里先用`mkfifo(3)`创建了一个FIFO文件，然后在子进程打开了FIFO文件，并使用`dup2(2)`把FIFO的文件描述符隐射到STDOUT_FILENO，之后对STDOUT_FILENO的操作就都会到FIFO文件里面；在父进程中打开FIFO文件并读取数据



FIFO文件打开方式

```c
open(const char *path, O_RDONLY);					// 此时open将会阻塞，除非有另一个进程以写的方式打开同一个FIFO，否则它不会返回
open(const char *path, O_RDONLY | O_NONBLOCK);		// 即使没有别的进程以写的方式打开FIFO，这个open调用也会成功并立刻返回
open(const char *path, O_WRONLY);					// 此时open将会阻塞，直到有另一个进程以读的方式打开同一个FIFO
open(const char *path, O_WRONLY | O_NONBLOCK);		// 这种情况将会立刻返回，但是如果没有进程以读的方式打开FIFO文件，open将会返回-1，并且FIFO文件也不会被打开，如果确实有一个进程以读的方式打开FIFO文件，那么就可以用它成功返回的文件描述符对FIFO文件进行写操作
```

