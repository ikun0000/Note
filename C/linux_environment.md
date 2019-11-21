# LInux 环境

### 命令行参数处理

摘自***Beginning  Linux Programming 4th Edition*** - 4.1.1实例

```c
#include <stdio.h>
#include <unistd.h>
#include <getopt.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    int opt;

    while ((opt = getopt(argc, argv, ":if:lr")) != -1)
    {
        switch (opt)
        {
        case 'i':
        case 'l':
        case 'r':
            printf("option: %c\n", opt);
            break;
            
        case 'f':
            printf("filename: %s\n", optarg);
            break;

        case ':':
            printf("option needs a value\n");
            break;

        case '?':
            printf("unknown option: %c\n", optopt);
            break;
        }
    }

    for (; optind < argc; optind++)
    {
        printf("argument: %s\n", argv[optind]);
    }

    return 0;
}
```

`getopt(3)`用于处理命令行参数（ [getopt(3)](http://man7.org/linux/man-pages/man3/getopt.3.html) ）

每执行一次`getopt(3)`就会解析返回有全局变量`optind`指向的参数，参数表由第三个参数指定，一个字母指定一个参数，字母后面跟着`':'`的代表需要一个参数

* 如果参数有关联的值，关联的值会放在全局变量`optarg`中
* 如果处理完毕，`getopt(3)`返回-1（特殊参数--会使`getopt(3)`停止扫描）
* 如果遇到无法识别的选项，`getopt(3)`返回`'?'`，并把它保存到全局变量`optopt`中
* 如果一个选项要求有关联值，但是执行时没有給，那么会返回`'?'`，如果将选项字符串的第一个设置为`':'`那么就不会返回`'?'`，而会返回`':'`，



摘自***Beginning  Linux Programming 4th Edition*** - 4.1.2实例

```c
#include <stdio.h>
#include <unistd.h>
#include <getopt.h>
#include <stdlib.h>

#define _GUN_SOURCE

int main(int argc, char *argv[])
{
    int opt;
    struct option longopts[] = {
        {"interface", 0, NULL, 'i'},
        {"file", 1, NULL, 'f'},
        {"list", 0, NULL, 'l'},
        {0, 0, 0, 0}
    };

    while ((opt = getopt_long(argc, argv, ":if:lr", longopts, NULL)) != -1)
    {
        switch (opt)
        {
        case 'i':
        case 'l':
        case 'r':
            printf("option: %c\n", opt);
            break;
            
        case 'f':
            printf("filename: %s\n", optarg);
            break;

        case ':':
            printf("option needs a value\n");
            break;

        case '?':
            printf("unknown option: %c\n", optopt);
            break;
        }
    }

    for (; optind < argc; optind++)
    {
        printf("argument: %s\n", argv[optind]);
    }

    return 0;
}
```

其实就是上面那个支持长参数的版本

`getopt_long(3)`用来处理长参数（[getopt_long(3)]( http://man7.org/linux/man-pages/man3/getopt.3.html )）

多出的第一个参数是指定参数的结构体，第二个参数写`NULL`就可以了

`option.has_arg`设置0代表不带参数，1代表必须有一个参数，2表示带一个可选参数

`option.flag`设置为`NULL`表示找到该选项返回`option.val`的值，一般就这么写就好了。否则返回0，并把`option.val`写到这里面

`option.val`为`getopt_long(3)`匹配成功返回的值



### 环境变量

```c
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[])
{
    if (argc == 1)
    {
        char **envs = __environ;
        char *env;

        while ((env = *envs++) != NULL)
        {
            printf("%s\n", env);
        }
    }
    else if (argc == 2)
    {
        char *env;

        if ((env = getenv(argv[1])) != NULL)
        {
            printf("%s=%s\n", argv[1], env);
        }
        else
        {
            printf("environ %s is not define\n", argv[1]);
        }
    }
    else
    {
        char env[100];
        memset(env, 0, 100);
        strncpy(env, argv[1], strlen(argv[1]));
        strncat(env, "=", 1);
        strncat(env, argv[2], strlen(argv[2]));

        if (putenv(env) != 0)
        {
            fprintf(stderr, "putenv(3) error\n");
            return -1;
        }
    }

    return 0;
}c
```

output

```
# ./a.out
GJS_DEBUG_TOPICS=JS ERROR;JS LOG
SSH_AUTH_SOCK=/run/user/0/keyring/ssh
SESSION_MANAGER=local/kali:@/tmp/.ICE-unix/1300,unix/kali:/tmp/.ICE-unix/1300
GNOME_TERMINAL_SCREEN=/org/gnome/Terminal/screen/ae524e01_f8ef_4ee9_b098_595c46aec966
SSH_AGENT_PID=1353
XDG_CURRENT_DESKTOP=GNOME
LANG=en_US.UTF-8
GPG_AGENT_INFO=/run/user/0/gnupg/S.gpg-agent:0:1
USER=root
GJS_DEBUG_OUTPUT=stderr
XDG_MENU_PREFIX=gnome-
DESKTOP_SESSION=gnome
HOME=/root
DBUS_SESSION_BUS_ADDRESS=unix:path=/run/user/0/bus
XDG_VTNR=2
XDG_SEAT=seat0
GTK_MODULES=gail:atk-bridge
XDG_DATA_DIRS=/usr/share/gnome:/usr/local/share/:/usr/share/
WINDOWPATH=2
XDG_SESSION_DESKTOP=gnome
QT_ACCESSIBILITY=1
GNOME_DESKTOP_SESSION_ID=this-is-deprecated
VTE_VERSION=5402
LOGNAME=root
GNOME_TERMINAL_SERVICE=:1.60
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
XDG_RUNTIME_DIR=/run/user/0
SHELL=/usr/bin/zsh
XDG_SESSION_ID=2
XDG_SESSION_TYPE=x11
USERNAME=root
GDM_LANG=en_US.UTF-8
COLORTERM=truecolor
XAUTHORITY=/run/user/0/gdm/Xauthority
PWD=/root
XDG_SESSION_CLASS=user
TERM=xterm-256color
GDMSESSION=gnome
DISPLAY=:1
SHLVL=1
OLDPWD=/root
ZSH=/root/.oh-my-zsh
PAGER=less
LESS=-R
LSCOLORS=Gxfxcxdxbxegedabagacad
LS_COLORS=rs=0:di=01;34:ln=01;36:mh=00:pi=40;33:so=01;35:do=01;35:bd=40;33;01:cd=40;33;01:or=40;31;01:mi=00:su=37;41:sg=30;43:ca=30;41:tw=30;42:ow=34;42:st=37;44:ex=01;32:*.tar=01;31:*.tgz=01;31:*.arc=01;31:*.arj=01;31:*.taz=01;31:*.lha=01;31:*.lz4=01;31:*.lzh=01;31:*.lzma=01;31:*.tlz=01;31:*.txz=01;31:*.tzo=01;31:*.t7z=01;31:*.zip=01;31:*.z=01;31:*.dz=01;31:*.gz=01;31:*.lrz=01;31:*.lz=01;31:*.lzo=01;31:*.xz=01;31:*.zst=01;31:*.tzst=01;31:*.bz2=01;31:*.bz=01;31:*.tbz=01;31:*.tbz2=01;31:*.tz=01;31:*.deb=01;31:*.rpm=01;31:*.jar=01;31:*.war=01;31:*.ear=01;31:*.sar=01;31:*.rar=01;31:*.alz=01;31:*.ace=01;31:*.zoo=01;31:*.cpio=01;31:*.7z=01;31:*.rz=01;31:*.cab=01;31:*.wim=01;31:*.swm=01;31:*.dwm=01;31:*.esd=01;31:*.jpg=01;35:*.jpeg=01;35:*.mjpg=01;35:*.mjpeg=01;35:*.gif=01;35:*.bmp=01;35:*.pbm=01;35:*.pgm=01;35:*.ppm=01;35:*.tga=01;35:*.xbm=01;35:*.xpm=01;35:*.tif=01;35:*.tiff=01;35:*.png=01;35:*.svg=01;35:*.svgz=01;35:*.mng=01;35:*.pcx=01;35:*.mov=01;35:*.mpg=01;35:*.mpeg=01;35:*.m2v=01;35:*.mkv=01;35:*.webm=01;35:*.ogm=01;35:*.mp4=01;35:*.m4v=01;35:*.mp4v=01;35:*.vob=01;35:*.qt=01;35:*.nuv=01;35:*.wmv=01;35:*.asf=01;35:*.rm=01;35:*.rmvb=01;35:*.flc=01;35:*.avi=01;35:*.fli=01;35:*.flv=01;35:*.gl=01;35:*.dl=01;35:*.xcf=01;35:*.xwd=01;35:*.yuv=01;35:*.cgm=01;35:*.emf=01;35:*.ogv=01;35:*.ogx=01;35:*.aac=00;36:*.au=00;36:*.flac=00;36:*.m4a=00;36:*.mid=00;36:*.midi=00;36:*.mka=00;36:*.mp3=00;36:*.mpc=00;36:*.ogg=00;36:*.ra=00;36:*.wav=00;36:*.oga=00;36:*.opus=00;36:*.spx=00;36:*.xspf=00;36:
P9K_SSH=0
_=/root/./a.out
# ./a.out PATH
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
# ./a.out JAVA_HOME /usr/bin/java
# ./a.out JAVA_HOME
environ JAVA_HOME is not define
```

`putenv(3)`只能影响到程序里面的环境变量，并不能影响到外部环境



### 时间和日期

参考：[菜鸟教程 - C 标准库 - <time.h>]( https://www.runoob.com/cprogramming/c-standard-library-time-h.html )



### 临时文件

使用`tmpnam(3)`随机生成一个文件路径（[tmpnam(3)]( http://man7.org/linux/man-pages/man3/tmpnam.3.html )）

使用`tmpfile(3)`返回一个临时文件的文件指针，程序结束后会自动删除（[tmpfile(3)]( http://man7.org/linux/man-pages/man3/tmpfile.3.html )）



### 用户/组信息

需要在程序中获取执行这个程序的用户的用户ID使用`getuid(2)`获取，获取登录名使用`getlogin(3)` ，获取进程用户组ID使用`getgid(2)`（[getuid(2)]( http://man7.org/linux/man-pages/man2/getuid.2.html )、[getlogin(3)]( http://man7.org/linux/man-pages/man3/getlogin.3.html )、[getgid(2)]( http://man7.org/linux/man-pages/man2/getgid.2.html )）



获取用户信息

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pwd.h>
#include <sys/types.h>
#include <ctype.h>
#include <shadow.h>
#include <grp.h>

int isnumber(const char *ptr)
{
    while (!isdigit(*(ptr++)) && *ptr != 0)
    {
        return 0;
    }
    return 1;
}

int main(int argc, char *argv[])
{
    struct passwd *userinfo;
    struct group *groupinfo;
    struct spwd *shadowinfo;

    if (argc == 1)
    {
        setpwent();
        while ((userinfo = getpwent()) != NULL)
        {
            printf("\n");
            printf("User ID: %ld\n", userinfo->pw_uid);
            printf("Username: %s\n", userinfo->pw_name);
            printf("Password: %s\n", userinfo->pw_passwd);
            shadowinfo = getspnam(userinfo->pw_name);
            printf("\t> Encrypted password: %s\n", shadowinfo->sp_pwdp);
            printf("Group ID: %ld\n", userinfo->pw_gid);
            groupinfo = getgrgid(userinfo->pw_gid);
            printf("\t> Group name: %s\n", groupinfo->gr_name);
            printf("User information: %s\n", userinfo->pw_gecos);
            printf("User home directory: %s\n", userinfo->pw_dir);
            printf("User shell: %s\n", userinfo->pw_shell);
            printf("--------------------------------------------------------------\n");
        }
        endpwent();
    }
    else 
    {
        if (isnumber(argv[1]))
        {
            if ((userinfo = getpwuid(atoi(argv[1]))) != NULL)
            {
                printf("\n");
                printf("User ID: %ld\n", userinfo->pw_uid);
                printf("Username: %s\n", userinfo->pw_name);
                printf("Password: %s\n", userinfo->pw_passwd);
                shadowinfo = getspnam(userinfo->pw_name);
                printf("\t> Encrypted password: %s\n", shadowinfo->sp_pwdp);
                printf("Group ID: %ld\n", userinfo->pw_gid);
                groupinfo = getgrgid(userinfo->pw_gid);
                printf("\t> Group name: %s\n", groupinfo->gr_name);
                printf("User information: %s\n", userinfo->pw_gecos);
                printf("User home directory: %s\n", userinfo->pw_dir);
                printf("User shell: %s\n", userinfo->pw_shell);
                printf("--------------------------------------------------------------\n");              
            }
        }
        else
        {
            if ((userinfo = getpwnam(argv[1])) != NULL)
            {
                printf("\n");
                printf("User ID: %ld\n", userinfo->pw_uid);
                printf("Username: %s\n", userinfo->pw_name);
                printf("Password: %s\n", userinfo->pw_passwd);
                shadowinfo = getspnam(userinfo->pw_name);
                printf("\t> Encrypted password: %s\n", shadowinfo->sp_pwdp);
                printf("Group ID: %ld\n", userinfo->pw_gid);
                groupinfo = getgrgid(userinfo->pw_gid);
                printf("\t> Group name: %s\n", groupinfo->gr_name);
                printf("User information: %s\n", userinfo->pw_gecos);
                printf("User home directory: %s\n", userinfo->pw_dir);
                printf("User shell: %s\n", userinfo->pw_shell);
                printf("--------------------------------------------------------------\n");              
            }
        }
    }

    return 0;
}
```

例子获取了用户信息，影子文件信息，组信息（[getpwuid(3)]( http://man7.org/linux/man-pages/man3/getpwuid.3p.html )、[getspnam(3)]( http://man7.org/linux/man-pages/man3/getspnam.3.html )、[getgrnam(3)]( http://man7.org/linux/man-pages/man3/getgrnam.3.html )）



实现`uname -a`功能

```c
#include <stdio.h>
#include <unistd.h>
#include <sys/utsname.h>

int main()
{
    struct utsname hostinfo;

    if (uname(&hostinfo) != 0)
    {
        fprintf(stderr, "uname(2) error\n");
        return -1;
    }

    printf("%s %s %s %s %s\n", hostinfo.sysname, hostinfo.nodename,
           hostinfo.release, hostinfo.version, hostinfo.machine);

    return 0;
}
```

`uname(1)`就是使用`uname(2)`实现的（[uname(2)](https://manpages.debian.org/testing/manpages-dev/uname.2.en.html )）



### 日志

参考：[man7.org - syslog(3)]( http://man7.org/linux/man-pages/man3/syslog.3.html )

摘自***Beginning  Linux Programming 4th Edition*** - 4.7实例

```c
#include <stdio.h>
#include <syslog.h>

int main()
{
    FILE *f;

    f = fopen("not_here", "r");
    if (!f)
    {
        syslog(LOG_ERR | LOG_USER, "open - %m\n");
    }

    return 0;
}
```

***Beginning  Linux Programming 4th Edition***日志信息输出到`/var/log/messages`中，但在我的环境下没有



### 资源和限制

C标准库limit：[菜鸟教程 - C 标准库 - <limits.h>]( https://www.runoob.com/cprogramming/c-standard-library-limits-h.html )

获取进程消耗的CPU时间和用户时间



摘自***Beginning  Linux Programming 4th Edition*** - 4.8实例

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <sys/resource.h>
#include <sys/time.h>
#include <sys/types.h>

void work()
{
    FILE *f;
    int i;
    double x = 4.5;

    f = tmpfile();
    for (i = 0; i < 10000; i++)
    {
        fprintf(f, "Do some output\n");
        if (ferror(f))
        {
            fprintf(stderr, "Error writing to temporary file\n");
            exit(1);
        }
    }

    for (i = 0; i < 1000000; i++)
    {
        x = log(x * x + 3.21);
    }
}

int main()
{
    struct rusage r_usage;
    struct rlimit r_limit;
    int priority;

    work();
    getrusage(RUSAGE_SELF, &r_usage);

    printf("CPU usage: User = %ld.%6ld, CPU = %ld.%6ld\n", 
           r_usage.ru_utime.tv_sec, r_usage.ru_utime.tv_usec,
           r_usage.ru_stime.tv_sec, r_usage.ru_stime.tv_usec);

    priority = getpriority(PRIO_PROCESS, getpid());
    printf("Current priority = %d\n", priority);
    printf("Setting priority to 4\n");
    setpriority(PRIO_PROCESS, getpid(), 4);
    priority = getpriority(PRIO_PROCESS, getpid());
    printf("Current priority = %d\n", priority);


    getrlimit(RLIMIT_FSIZE, &r_limit);
    printf("Current FSIZE limit: soft = %ld, hard = %ld\n",
           r_limit.rlim_cur, r_limit.rlim_max);

    r_limit.rlim_cur = 2048;
    r_limit.rlim_max = 4096;
    printf("Setting a 2K file size limit\n");
    setrlimit(RLIMIT_FSIZE, &r_limit);

    work();
    return 0;
}
```

GCC编译

```
# gcc a.c -lm
```

***如果不这样编译会出错***

`getrusage(2)`获取程序耗费的**用户时间**（程序执行自身指令所耗费的时间）和**系统时间**（操作系统为程序执行所耗费的时间，就是执行系统函数的时间）（[getrusage(2)]( http://man7.org/linux/man-pages/man2/getrusage.2.html )）

`getpriority(2)`和`setpriority(2)`获取和设置进程、进程组、用户的优先级（[getpriority(2)]( http://man7.org/linux/man-pages/man2/getpriority.2.html )、[setpriority(2)]( http://man7.org/linux/man-pages/man2/getpriority.2.html )）

`getrlimit(2)`和`setrlimit(2)`则获取和设置系统诸如内核存储文件大小、CPU限制、数据段限制、文件大小、可打开文件数、栈空间大小、地址空间的限制（[getrlimit(2)]( http://man7.org/linux/man-pages/man2/getrlimit.2.html )、[setrlimit(2)]( http://man7.org/linux/man-pages/man2/getrlimit.2.html )）