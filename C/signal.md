# 信号

摘自***Beginning  Linux Programming 4th Edition*** - 11.4实例

```c
#include <stdio.h>
#include <signal.h>
#include <unistd.h>

void ouch(int sig)
{
    printf("OUCH! - I got signal %d\n", sig);
    (void)signal(SIGINT, SIG_DFL);
}

int main()
{
    (void)signal(SIGINT, ouch);

    while (1)
    {
        printf("Hello world!\n");
        sleep(1);
    }

    return 0;
}
```

注册一个`SIGINT`的信号处理函数（[signal(2)]( http://man7.org/linux/man-pages/man2/signal.2.html )）



低配版`kill(1)`

```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("usage: a.out <signal> <pid>\n");
        return 0;
    }

    kill(atoi(argv[2]), atoi(argv[1]));

    return 0;
}
```

`kill(2)`向进程发送信号（[kill(2)]( http://man7.org/linux/man-pages/man2/kill.2.html )）



摘自***Beginning  Linux Programming 4th Edition*** - 11.4.1实例（有修改）

```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>

static int alarm_fired = 0;

void ding(int sig)
{
    alarm_fired = 1;
}

int main(int argc, char *argv[])
{
    pid_t pid;

    if (argc != 2)
    {
        printf("usage: a.out <second>\n");
        return 0;
    }

    printf("starting...\n");

    if ((pid = fork()) < 0)
    {
        perror("fork(2) failed");
        exit(1);
    }
    else if (pid == 0)
    {
        sleep(atoi(argv[1]));
        kill(getppid(), SIGALRM);
        exit(0);
    }
    
    (void)signal(SIGALRM, ding);
    pause();
    if (alarm_fired)
    {
        printf("Ding!\n");
    }

    printf("done\n");
    return 0;
}
```

父进程用`pause(2)`等待子进程发送`SIGALRM`n信号（[pause(2)]( http://man7.org/linux/man-pages/man2/pause.2.html )）

`alarm(2)`则可以在每隔一段时间向自己发送`SIGALRM`信号（[alarm(2)]( http://man7.org/linux/man-pages/man2/alarm.2.html )）



### 使用`sigaction`

摘自***Beginning  Linux Programming 4th Edition*** - 11.4.1实例

```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

void ouch(int sig)
{
    printf("OUCH! - I got signal %d\n", sig);
}

int main()
{
    struct sigaction act;

    act.sa_handler = ouch;
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    sigaction(SIGINT, &act, 0);

    while (1)
    {
        printf("Hello world!\n");
        sleep(1);
    }

    return 0;
}
```

这里使用`sigaction(2)`代替`signal(2)`（[sigaction(2)]( http://man7.org/linux/man-pages/man2/sigaction.2.html )）

