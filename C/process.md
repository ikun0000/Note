# 进程

C标准库提供`system(3)`启动一个进程（[system(3)]( http://man7.org/linux/man-pages/man3/system.3.html )）

获取当前进程ID（[getpid(2)]( http://man7.org/linux/man-pages/man2/getpid.2.html )）

获取父进程ID（[getppid()]( http://man7.org/linux/man-pages/man2/getpid.2.html )）



### 子进程

一个不知道有什么用的例子，凑合一下吧，实在想不出有什么例子

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <syslog.h>
#include <sys/wait.h>

char *w[] = {
    "w",
    "-f",
    NULL
};

int main()
{
    pid_t pid;
    int status;

    if ((pid = fork()) < 0)
    {
        fprintf(stderr, "fork(2) error\n");
        exit(1);
    }
    else if (pid == 0)
    {
        execl("/bin/finger", "finger", "-lmps", (char *)0);
    }
    else
    {
        waitpid(pid, &status, 0);
        if (!WIFEXITED(status))
        {
            syslog(LOG_WARNING, "%d have exit unnormal %m", pid);
        }
        
        printf("\n");
        execv("/bin/w", w);
    }

    return 0;
}
```

`fork(2)`系统调用创建一个子进程，这个函数会返回两次，在子进程的返回值是0，在父进程的返回值是子进程的进程ID，此时父子进程的环境是一样的（[fork(2)]( http://man7.org/linux/man-pages/man2/fork.2.html )）

`exec`函数族用来替换当前进程的进程镜像到新的进程镜像，但在所有`exec`函数中，只有`execve(2)`是系统调用，其它`exec`函数都是在调用`execve(2)`

`exec`函数带有`l`的表示以参数表（list）的方式传入参数，带有`v`  的表示使用指针数组（vector）方式传入参数，使用参数表时最后的参数后边要跟着`(char *)0`，使用指针数组时最后一项要是`NULL`。第一个参数是文件名，就是`arg0`在程序里面是`argv[0]`，这个要和`pathname`或者`file`的最后一项一样

`exec`函数带有`e`的表示带入指定的环境变量（用一个指针数组传入，格式为`env=value`）

`exec`函数带有`p`的表示`exec`函数的第一个参数直接写二进制文件名，`exec`函数会在`PATH`环境变量中查找路径

wait(2)和`waitpid(2)`用来等待进程结束（[wait(2)、waitpid(2)]( http://man7.org/linux/man-pages/man2/waitpid.2.html )）

`exit(3)` `_exit(2)`退出进程（[exit(3)]( http://man7.org/linux/man-pages/man3/exit.3.html )，[_exit(2)](http://man7.org/linux/man-pages/man2/exit.2.html)）

`atexit(3)`用来注册`exit(3)`调用时执行的函数