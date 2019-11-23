# System V IPC

### 信号量

```c
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <fcntl.h>
#include <sys/sem.h>
#include <sys/ipc.h>
#include <sys/types.h>
#include <sys/stat.h>

union semun {
    int              val;    /* Value for SETVAL */
    struct semid_ds *buf;    /* Buffer for IPC_STAT, IPC_SET */
    unsigned short  *array;  /* Array for GETALL, SETALL */
    struct seminfo  *__buf;  /* Buffer for IPC_INFO
                                (Linux-specific) */
};

#define DATAFILE    "data.file"

static int sem_id;

int set_semvalue()
{
    union semun sem_union;
    sem_union.val = 1;
    return semctl(sem_id, 0, SETVAL, sem_union);
}

int del_semvalue()
{
    union semun sem_union;
    return semctl(sem_id, 0, IPC_RMID, sem_union);
}

int semaphore_p()
{
    struct sembuf sem_b;
    sem_b.sem_num = 0;
    sem_b.sem_op = -1;
    sem_b.sem_flg = SEM_UNDO;
    return semop(sem_id, &sem_b, 1);
}

int semaphore_v()
{
    struct sembuf sem_b;
    sem_b.sem_num = 0;
    sem_b.sem_op = 1;
    sem_b.sem_flg = SEM_UNDO;
    return semop(sem_id, &sem_b, 1);
}

void *thread_func(void *ptr)
{
    if (semaphore_p() == -1)
    {
        fprintf(stderr, "semop(2) P operation error\n");
        exit(-2);
    }
    
    FILE *fp;
    time_t tm;
    if ((fp = fopen(DATAFILE, "a")) == NULL)
    {
        fprintf(stderr, "fopen(3) error\n");
        exit(-3);
    }
    time(&tm);
    fprintf(fp, "[%s] - %ld\n", ctime(&tm), pthread_self());
    fclose(fp);

    if (semaphore_v() == -1)
    {
        fprintf(stderr, "semop(2) V operation error\n");
        exit(-2);
    }
    pthread_exit(NULL);
}

int main()
{
    pthread_t threadid[5];
    int i;

    sem_id = semget((key_t)1234, 1, 0666 | IPC_CREAT);
    if (set_semvalue() == -1)
    {
        fprintf(stderr, "semctl(2) error\n");
        return -1;
    }

    for (i = 0; i < 5; i++)
    {
        pthread_create(&threadid[i], NULL, thread_func, NULL);
    }
    for (i = 0; i < 5; i++)
    {
        pthread_join(threadid[i], NULL);
    }

    if (del_semvalue() == -1)
    {
        fprintf(stderr, "semctl(2) error\n");
        return -1;
    }

    return 0;
}
```

这里使用信号量来控制每次只有一个线程可以打开data.file进行写入操作

首先在主函数获取信号量ID，然后用`semctl(2)`（被封装过的）调用`command`参数设为`SETVAL`来初始化信号量，在使用信号量之前必须这样做，在主函数最后再次使用`semctl(2)`（被封装过的）调用`command`参数设为`IPC_RMID`来删除信号量（[semget(2)](http://man7.org/linux/man-pages/man2/semget.2.html)、[semctl(2)](http://man7.org/linux/man-pages/man2/semctl.2.html)）

在线程函数内开始调用`semop(2)`（被封装过的）执行P操作，然后对data.file进行写入操作，在离开之前再调用`semop(2)`（被封装过的）执行V操作（[semop(2)](http://man7.org/linux/man-pages/man2/semop.2.html)）

**`semctl(2)`会使用到`union semun`，但是这个在有些系统的头文件没有定义，需要在程序里面自己定义**

> P(semaphore value)：用于等待；如果信号量变量大于0，就-1；如果他的值等于0，就挂起该进程/线程的执行
>
> V(semaphore value)：用于发送信号；如果别的进程/线程因等待而被挂起，就让它回复运行；如果没有进程因等待而被挂起，就给他+1



### 二进制信号量

这个是上一个例子一样，就是换了

```c
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <semaphore.h>

#define DATAFILE    "data.file"

static sem_t sem_id;

void *thread_func(void *ptr)
{
    if (sem_wait(&sem_id) == -1)
    {
        fprintf(stderr, "sem_wait(3) error\n");
        exit(-2);
    }
    
    FILE *fp;
    time_t tm;
    if ((fp = fopen(DATAFILE, "a")) == NULL)
    {
        fprintf(stderr, "fopen(3) error\n");
        exit(-3);
    }
    time(&tm);
    fprintf(fp, "[%s] - %ld\n", ctime(&tm), pthread_self());
    fclose(fp);

    if (sem_post(&sem_id) == -1)
    {
        fprintf(stderr, "sem_post(3) error\n");
        exit(-2);
    }
    pthread_exit(NULL);
}

int main()
{
    pthread_t threadid[5];
    int i;

    if (sem_init(&sem_id, 0, 1) != 0)
    {
        fprintf(stderr, "sem_init(3) error");
        exit(-1);
    }

    for (i = 0; i < 5; i++)
    {
        pthread_create(&threadid[i], NULL, thread_func, NULL);
    }
    for (i = 0; i < 5; i++)
    {
        pthread_join(threadid[i], NULL);
    }
    
    sem_destroy(&sem_id);

    return 0;
}
```

这里是用了`sem_init(3)`来初始化信号量，**第三个参数指定了初始化信号量的值，如果设成0，那么第一个线程调用`sem_wait(3)`就会一直等待，这里设成了1**（[sem_init(3)](http://man7.org/linux/man-pages/man3/sem_init.3.html)、[sem_destroy(3)](http://man7.org/linux/man-pages/man3/sem_destroy.3.html)、[sem_wait(3)](http://man7.org/linux/man-pages/man3/sem_wait.3.html)、[sem_post(3)](http://man7.org/linux/man-pages/man3/sem_post.3.html)）



**使用了这几个函数时要这样编译**

```
# gcc a.c -lpthread
```



### 共享内存

又是没卵用的demo，还要两个，双倍的快乐

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>

#define LOG_FILE    "file.log"

static int shmid;

int main()
{
    int counter = 0;
    FILE *fp;
    void *shared_memory;

    if ((shmid = shmget((key_t)1234, 128, 0666 | IPC_CREAT)) == -1)
    {
        fprintf(stderr, "shmget(2) error\n");
        exit(-1);
    }
    if ((shared_memory = shmat(shmid, (void *)0, 0)) == (void *)-1)
    {
        fprintf(stderr, "shmat(2) error\n");
        exit(-1);
    }
    if ((fp = fopen(LOG_FILE, "w")) == NULL)
    {
        fprintf(stderr, "fopen(3) error\n");
        exit(-1);
    }

    while (counter < 10)
    {
        sleep(3);
        fprintf(fp, "%s\n", (char *)shared_memory);
        counter++;
    }
    
    if (shmdt(shared_memory) == -1)
    {
        fprintf(stderr, "shmdt(2) error\n");
        exit(-1);
    }
    if (shmctl(shmid, IPC_RMID, 0) == -1)
    {
        fprintf(stderr, "shmctl(2) error\n");
        exit(-1);
    }

    fclose(fp);
    return 0;
}
```

首先还是用`shmget(2)`创建一段共享内存，然后`shmat(2)`把它附加到程序的内存空间上，然后就可以快乐的读写了，然后再解除附加和删除它（[shmget(2)](http://man7.org/linux/man-pages/man2/shmget.2.html)、[shmat(2)](http://man7.org/linux/man-pages/man3/shmat.3p.html)、[shmdt(2)](http://man7.org/linux/man-pages/man3/shmdt.3p.html)、[shmctl(2)](http://man7.org/linux/man-pages/man2/shmctl.2.html)）



```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>

int shmid;

int main()
{
    void *shared_memory;

    if ((shmid = shmget((key_t)1234, 128, 0666 | IPC_CREAT)) == -1)
    {
        fprintf(stderr, "shmget(2) error\n");
        exit(-1);
    }
    if ((shared_memory = shmat(shmid, (void *)0, 0)) == (void *)-1)
    {
        fprintf(stderr, "shmat(2) error\n");
        exit(-1);
    }

    strncpy((char *)shared_memory, "GET", 128);
    sleep(10);
    strncpy((char *)shared_memory, "POST", 128);

    if (shmdt(shared_memory) == -1)
    {
        fprintf(stderr, "shmdt(2) error\n");
        exit(-1);
    }
    if (shmctl(shmid, IPC_RMID, 0) == -1)
    {
        fprintf(stderr, "stmctl(2) error\n");
        exit(-1);
    }

    return 0;
}
```

同上



### 消息队列

摘自***Beginning  Linux Programming 4th Edition*** - 14.3实例

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>

struct my_msg_st {
    long int my_msg_type;
    char some_text[BUFSIZ];
};

int main()
{
    int running = 1;
    int msgid;
    struct my_msg_st some_data;
    long int msg_to_receive = 0;

    msgid = msgget((key_t)1234, 0666 | IPC_CREAT);

    if (msgid == -1)
    {
        fprintf(stderr, "msgget failed with error: %d\n", errno);
        exit(EXIT_FAILURE);
    }

    while (running)
    {
        if (msgrcv(msgid, (void  *)&some_data, BUFSIZ, msg_to_receive, 0) == -1)
        {
            fprintf(stderr, "msgrcv failed with error: %d\n", errno);
            exit(EXIT_FAILURE);
        }
        printf("You wrote %s", some_data.some_text);
        if (strncmp(some_data.some_text, "end", 3) == 0)
        {
            running = 0;
        }
    }

    if (msgctl(msgid, IPC_RMID, 0) == -1)
    {
        fprintf(stderr, "msgctl(IPC_RMID) failed\n");
        exit(EXIT_FAILURE);
    }

    return 0;
}
```



```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#define MAX_TEXT 512

struct my_msg_st {
    long int my_msg_type;
    char some_text[BUFSIZ];
};

int main()
{
    int running = 1;
    int msgid;
    struct my_msg_st some_data;
    char buffer[BUFSIZ];

    msgid = msgget((key_t)1234, 0666 | IPC_CREAT);

    if (msgid == -1)
    {
        fprintf(stderr, "msgget failed with error: %d\n", errno);
        exit(EXIT_FAILURE);
    }

    while (running)
    {
        printf("Enter some text: ");
        fgets(buffer, BUFSIZ, stdin);
        some_data.my_msg_type = 1;
        strncpy(some_data.some_text, buffer, BUFSIZ);

        if (msgsnd(msgid, (void *)&some_data, MAX_TEXT, 0) == -1)
        {
            fprintf(stderr, "msgsend failed\n");
            exit(EXIT_FAILURE);
        }

        if (strncmp(buffer, "end", 3) == 0)
        {
            running = 0;
        }
    }

    if (msgctl(msgid, IPC_RMID, 0) == -1)
    {
        fprintf(stderr, "msgctl(IPC_RMID) failed\n");
        exit(EXIT_FAILURE);
    }

    return 0;
}
```

`msgget(2)`首先创建一个消息队列（[msgget(2)](http://man7.org/linux/man-pages/man2/msgget.2.html)）

`msgsnd(2)`用来发送消息第二个参数是消息结构，它有两个限制。首先，它的长度必须小于系统规定的上限；其次，它必须以一个长整形成员变量开始，接收函数将用这个长整形变量判断消息类型。当使用消息时，最好把消息结构定义为下面这样

```c
struct msgbuf {
    long mtype;		/* message type, must be > 0 */
    char mtext[1];	/* message data */
};

struct msgbuf {
    long int mtype;
    /* The data you wish transfer */
};
```

由于在接收消息时要用到`mtype`，所以不能忽略它，所以在声明消息结构时必须包含它，初始化已知值（[msgsnd(2)](http://man7.org/linux/man-pages/man3/msgsnd.3p.html)）

第一个参数是消息队列的标识符

第二个参数是一个指向准备发送消息的结构的指针

第三个参数是消息的长度，这个长度不能包含长整型的消息类型变量的长度

第四个参数是控制当消息队列满的时候或者达到系统限制是发生的事情，当设置成`IPC_NOWAIT`时，函数将立刻返回，不发送消息并返回-1。如果`IPC_NOWAIT`标志被清除，则发送消息的进程将会挂起直到队列腾出可用空间

`msgrcv(2)`则从消息队列获取消息（[msgrcv(2)](http://man7.org/linux/man-pages/man3/msgrcv.3p.html)）

第一个参数和`msgsnd(2)`一样

第二个参数也和`msgsnd(2)`

第三个参数也和`msgsnd(2)`一样，就是不包括第一个长整型消息类型的消息长度

第四个参数可以实现一个简单的接收优先级，如果它的值为0，就获取队列中第一个可用的消息；如果它的值大于0，则获取具有相同消息类型的第一个消息；如果它的值小于0，则获取消息类型等于或小于它的绝对值的第一个消息

第五个参数指定当队列没有相应类型消息可以接收时发生的事情，如果`IPC_NOWAIT`标志被设置，则函数立刻返回，返回值为-1，如果`IPC_NOWAIT`标志被清除，进程将被挂起知道有一条和接收消息类型匹配的消息

`msgctl(2)`在这里被用来关闭消息队列（[msgctl(2)](http://man7.org/linux/man-pages/man2/msgctl.2.html)）