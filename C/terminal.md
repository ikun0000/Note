# 终端

### 判断当前描述符是否关联终端

```c
if (isatty(STDOUT_FILENO))
{
	// TODOc
}
```

上面的代码用来测试STDOUT_FILENO是否关联一个终端（[isatty(3)](http://man7.org/linux/man-pages/man3/isatty.3p.html)）



## 因为终端有好多东西讲，但是好像好多一般情况用不到，所以现在只做了两个比较有用的例子

### 密码程序

摘自***Beginning  Linux Programming 4th Edition*** - 5.4.7实例

```c
#include <stdio.h>
#include <stdlib.h>
#include <termios.h>

#define PASSWORD_LEN    8

int main()
{
    struct termios initialrsettings, newrsettings;
    char password[PASSWORD_LEN];

    tcgetattr(fileno(stdin), &initialrsettings);

    newrsettings = initialrsettings;
    newrsettings.c_lflag &= ~ECHO;

    printf("Enter password: ");

    if (tcsetattr(fileno(stdin), TCSAFLUSH, &newrsettings) != 0)
    {
        fprintf(stderr, "Could not set attributes\n");
    }
    else
    {
        fgets(password, PASSWORD_LEN, stdin);
        tcsetattr(fileno(stdin), TCSANOW, &initialrsettings);
        fprintf(stdout, "\nYou entered %s\n", password);
    }

    return 0;
}
```

这个程序首先获取标准输入的`termios`结构，然后修改`termios.c_lflag`去除`ECHO`标志位，即关闭输入回显，然后设置新的`termios`到标准输入（[tcgetattr(3)](https://linux.die.net/man/3/tcgetattr)、[tcsetattr(3)](https://linux.die.net/man/3/tcsetattr)）



### 实时读取输入

```c
#include <stdio.h>
#include <stdlib.h>
#include <termios.h>

int getch(FILE *f)
{
    int c;
    struct termios init_termios, new_termios;
    tcgetattr(fileno(f), &init_termios);

    new_termios = init_termios;
    new_termios.c_lflag &= ~ICANON;
    new_termios.c_lflag &= ~ECHO;
    new_termios.c_cc[VMIN] = 1;
    new_termios.c_cc[VTIME] = 0;
    new_termios.c_lflag &= ~ISIG;
    if (tcsetattr(fileno(f), TCSANOW, &new_termios) != 0)
    {
        return -1;
    }

    c = fgetc(f);

    tcsetattr(fileno(f), TCSANOW, &init_termios);
    return c; 
}

int main()
{
    int c;
    char buffer[100];
    FILE *fp;

    do {
        printf("[r] - uname -r\n");
        printf("[s] - uname -s\n");
        printf("[a] - uname -a\n");
        printf("[q] - quit\n");
        printf(">> ");
        c = getch(stdin);

        if (c == '\n' || c == '\r')
        {
            continue;
        }
        else if (c == 'r')
        {
            fp = popen("uname -r", "r");
            fgets(buffer, 100, fp);
            printf("%s\n", buffer);
            pclose(fp);
        }
        else if (c == 's')
        {
            fp = popen("uname -s", "r");
            fgets(buffer, 100, fp);
            printf("%s\n", buffer);
            pclose(fp);
        }
        else if (c == 'a')
        {
            fp = popen("uname -a", "r");
            fgets(buffer, 100, fp);
            printf("%s\n", buffer);
            pclose(fp);
        }
    } while (c != 'q');
    printf("\n");
    return 0;
}
```

`getch`实现实时输入的方法

1. 要完成实时读取，首先要关闭标准输入模式（`ICANON`标志）
2. 关闭输入回显（`ECHO`标志）
3. 设置特殊控制字符`VMIN`为1
4. 设置特殊控制字符`VTIME`为0
5. 关闭信号（`ISIG`标志）

> **注意点**：
>
> 在非标准模式下，默认的回车和换行符之间的映射已不存在了，所以要对回车符`\r`进行检查
>
> 设置`ISIG`标志，程序将忽略处理诸如`^C`、`^D`之类的特殊字符



特殊控制字符`	TIME`和`MIN`

* `MIN = 0`和`TIME = 0`：`read(2)`调用总是立刻返回，如果有等待处理的字符，它们会立刻返回，如果没有字符等待处理，`read(2)`调用返回0，并且不读取任何字符
* `MIN = 0`和`TIME > 0`：只要有字符可以处理或者经过`TIME`个十分之一秒的时间间隔，`read(2)`调用就返回，如果因为超时而没有读取到任何字符，`read(2)`返回0，否则返回读取到的字符
* `MIN > 0`和`TIME = 0`：`read(2)`会一直等待，直到有`MIN`个字符可以读取时才返回，返回值是读取的字符数量，到达文件为返回0
* `MIN > 0`和`TIME = 0`：当`read(2)`被调用时，它会等待接收一个字符，在接收到第一个字符及后续的每个字符后，一个字符间隔定时器启动（如果一个定时器已经运行，则重启它），当有`MIN`个字符可读或两个字符的时间间隔超过`TIME`个十分之一秒时，`read(2)`调用返回。这个功能可以区分单独按下`Escape`键还是按下一个以`Escape`键开始的功能组合键。但要注意的是，网络通信或处理器的高负载将使得类似的定时器失去作用

