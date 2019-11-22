# POSIX线程

### 线程创建

又是毫无卵用的例子

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <unistd.h>

struct thread_info {
    int sleep_time;
    int thread_num;
};

void *thread_proc(void *val)
{
    struct thread_info *threadinfo = (struct thread_info *)val;
    sleep(threadinfo->sleep_time);
    printf("NO.%d thread, thread id is %ld\n", threadinfo->thread_num, (long)pthread_self());
    pthread_exit(NULL);
}

int main()
{
    int i;
    time_t t;
    pthread_t threads[10];
    struct thread_info threadinfos[10];

    time(&t);
    srand((unsigned int)t);
    for (i = 0; i < 10; i++)
    {
        threadinfos[i].thread_num = i;
        threadinfos[i].sleep_time = rand() % 5;
        pthread_create(&threads[i], NULL, thread_proc, (void *)&threadinfos[i]);    
    }

    for (i = 0; i < 10; i++)
    {
        pthread_join(threads[i], NULL);    
    }

    return 0;
}
```

`pthread_create(3)`创建线程（[pthread_create(3)]( http://man7.org/linux/man-pages/man3/pthread_create.3.html )）

`pthread_exit(3)`退出线程（[pthread_exit(3)]( http://man7.org/linux/man-pages/man3/pthread_exit.3.html )）

`pthread_join(3)`等待线程结束（[pthread_join(3)]( http://man7.org/linux/man-pages/man3/pthread_join.3.html )）

`pthread_self(3)`获取线程的线程ID（[pthread_self(3)]( http://man7.org/linux/man-pages/man3/pthread_self.3.html )）

`pthread_equal(3)`比较线程ID（[pthread_equal(3)]( http://man7.org/linux/man-pages/man3/pthread_equal.3.html )）

`pthread_cancel(3)`向线程发送关闭信号（[pthread_cancel(3)]( http://man7.org/linux/man-pages/man3/pthread_cancel.3.html )）



编译多线程程序

```
# gcc -lpthread a.c
```



### 线程同步 - 互斥量

```c
#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

int numbers = 0;
pthread_mutex_t flag; 

void *thread_proc(void *ptr)
{
    pthread_mutex_lock(&flag);
    
    numbers = rand();
    printf("number is %d\n", numbers);

    pthread_mutex_unlock(&flag);
    pthread_exit(NULL);
}

int main()
{
    int i;
    pthread_t threads[5];
    
    srand((unsigned int)time(NULL));
    pthread_mutex_init(&flag, NULL);
    for (i = 0; i < 5; i++)
    {
        pthread_create(&threads[i], NULL, thread_proc, NULL);
    }

    for (i = 0; i < 5; i++)
    {
        pthread_join(threads[i], NULL);
    }

    pthread_mutex_destroy(&flag);
    return 0;
}
```

对`pthread_mutex_t`加锁之后，其它线程再次加锁就会处于挂起状态，直到原来的锁解除（[pthread_mutex_init(3)]( http://man7.org/linux/man-pages/man3/pthread_mutex_init.3p.html )、[pthread_mutex_destroy(3)]( http://man7.org/linux/man-pages/man3/pthread_mutex_destroy.3p.html )、[pthread_mutex_lock(3)]( http://man7.org/linux/man-pages/man3/pthread_mutex_lock.3p.html )、[pthread_mutex_unlock(3)]( http://man7.org/linux/man-pages/man3/pthread_mutex_lock.3p.html )、[pthread_mutex_trylock(3)]( http://man7.org/linux/man-pages/man3/pthread_mutex_trylock.3p.html )）



### 线程同步 - 读写锁

```c
#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

int numbers = 0;
pthread_rwlock_t flag;

void *thread_proc_read(void *ptr)
{
    pthread_rwlock_rdlock(&flag);
    printf("number is %d\n", numbers);
    pthread_rwlock_unlock(&flag);
    pthread_exit(NULL);
}

void *thread_proc_write(void *ptr)
{
    pthread_rwlock_wrlock(&flag);
    numbers = rand();
    pthread_rwlock_unlock(&flag);
    pthread_exit(NULL);
}

int main()
{
    int i;
    pthread_t threads_read[5];
    pthread_t threads_write[5];

    srand((unsigned int)time(NULL));
    pthread_rwlock_init(&flag, NULL);
    for (i = 0; i < 5; i++)
    {
        pthread_create(&threads_read[i], NULL, thread_proc_read, NULL);
        pthread_create(&threads_write[i], NULL, thread_proc_write, NULL);
    }

    for (i = 0; i < 5; i++)
    {
        pthread_join(threads_read[i], NULL);
        pthread_join(threads_write[i], NULL);
    }

    pthread_rwlock_destroy(&flag);
    return 0;
}
```

`pthread_rwlock_t`被加上读锁之后，其他线程仍然可以加上读锁，而不能加写锁。如果被加的是写锁，那么任何锁都不可以被加上了（[pthread_rwlock_init(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_rdlock.3p.html )、[pthread_rwlock_destroy(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_init.3p.html )、[pthread_rwlock_rdlock(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_rdlock.3p.html )、[pthread_rwlock_wrlock(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_wrlock.3p.html )、[pthread_rwlock_unlock(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_unlock.3p.html )、[pthread_rwlock_tryrdlock(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_tryrdlock.3p.html )、[pthread_rwlock_trywrlock(3)]( http://man7.org/linux/man-pages/man3/pthread_rwlock_trywrlock.3p.html )）

