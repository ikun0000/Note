# 套接字

### UNIX域协议（文件系统套接字）

摘自***Beginning  Linux Programming 4th Edition*** - 15.2实例

client

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>

int main()
{
    int sockfd;
    int len;
    struct sockaddr_un address;
    int result;
    char ch = 'A';

    sockfd = socket(AF_UNIX, SOCK_STREAM, 0);

    address.sun_family = AF_UNIX;
    strcpy(address.sun_path, "server_socket");
    len = sizeof(address);

    result = connect(sockfd, (struct sockaddr *)&address, len);
    if (result == -1)
    {
        perror("open: client");
        exit(1);
    }

    write(sockfd, &ch, 1);
    read(sockfd, &ch, 1);
    printf("char from server = %c\n", ch);
    close(sockfd);

    return 0;
}
```

客户端首先使用`socket(2)`创建UNIX套接字，然后初始化`struct sockaddr_un`结构然后连接，之后就可以像读写文件一样读写文件描述符。其它套接字的客户端也大概是这个框架（[socket(2)](http://man7.org/linux/man-pages/man2/socket.2.html)）

因为除了文件系统套接字，还有别的套接字，UNIX使用结构的长度来判断要创建的套接字，所以`connect(2)`和之后的`bind(2)` `accept(2)`都需要一个表示`struct sockaddr`的长度的参数或者长度参数的地址（[connect(2)](http://man7.org/linux/man-pages/man2/connect.2.html)）

server

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>

int main()
{
    int server_sockfd, client_sockfd;
    int server_len, client_len;
    struct sockaddr_un server_address;
    struct sockaddr_un client_address;

    unlink("server_socket");
    server_sockfd = socket(AF_UNIX, SOCK_STREAM, 0);
    server_address.sun_family = AF_UNIX;
    strcpy(server_address.sun_path, "server_socket");
    server_len = sizeof(server_address);
    bind(server_sockfd, (struct sockaddr *)&server_address, server_len);

    listen(server_sockfd, 5);
    while (1) 
    {
        char ch;
        printf("server waiting...\n");

        client_len = sizeof(client_address);
        client_sockfd = accept(server_sockfd, 
                               (struct sockaddr *)&client_address, (socklen_t *)&client_len);

        read(client_sockfd, &ch, 1);
        ch++;
        write(client_sockfd, &ch, 1);
        close(client_sockfd);
    }

    return 0;
}
```

而服务端首先也是使用`socket(2)`来创建套接字，然后用`bind(2)`绑定套接字到到监听地址，`listen(2)`设置最大连接数，循环使用`accept(2)`接收请求（[bind(2)](http://man7.org/linux/man-pages/man2/bind.2.html)、[listen(2)](http://man7.org/linux/man-pages/man2/listen.2.html)、[accept(2)](http://man7.org/linux/man-pages/man2/accept.2.html)）

其它套接字的服务端也大概是这种框架，但是这样只能在同一时间处理一个客户端，要同时处理多个客户端的请求，应该在循环里面创建每个客户端的处理线程



`struct sockaddr_un`定义

```c
struct sockaddr_un {
    sa_family_t		sun_family;		/* AF_UNIX */
    char			sun_path[];		/* Pathname */
};
```



### 流式套接字

摘自***Beginning  Linux Programming 4th Edition*** - 15.2.9实例

client

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>

int main()
{
    int sockfd;
    int len;
    struct sockaddr_in address;
    int result;
    char ch = 'A';

    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = inet_addr("127.0.0.1");
    address.sin_port = htons(10001);
    len = sizeof(address);

    result = connect(sockfd, (struct sockaddr *)&address, len);
    if (result == -1)
    {
        perror("open: client");
        exit(1);
    }

    write(sockfd, &ch, 1);
    read(sockfd, &ch, 1);
    printf("char from server = %c\n", ch);
    close(sockfd);

    return 0;
}
```

server

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>

int main()
{
    int server_sockfd, client_sockfd;
    int server_len, client_len;
    struct sockaddr_in server_address;
    struct sockaddr_in client_address;

    server_sockfd = socket(AF_INET, SOCK_STREAM, 0);
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(10001);
    server_len = sizeof(server_address);
    bind(server_sockfd, (struct sockaddr *)&server_address, server_len);

    listen(server_sockfd, 5);
    while (1) 
    {
        char ch;
        printf("server waiting...\n");

        client_len = sizeof(client_address);
        client_sockfd = accept(server_sockfd, 
                               (struct sockaddr *)&client_address, (socklen_t *)&client_len);

        read(client_sockfd, &ch, 1);
        ch++;
        write(client_sockfd, &ch, 1);
        close(client_sockfd);
    }

    return 0;
}
```

client和server和之前的文件系统套接字差不多，只是`socket(2)`创建套接字时第一个参数改成`AF_INET`创建IPv4网络地址的套接字，或者使用`AF_INET6`创建IPv6网络地址的套接字

而套接字地址结构变成`struct sockaddr_in`，它的定义如下

```c
struct sockaddr_in {
    short int				sin_family;		/* AF_INET */
    unsigned short int		sin_port;		/* Port number */
    struct in_addr			sin_addr;		/* Internet address */
};

struct in_addr {
    unsigned long int 		s_addr;
};
```



IPv6版本

修改自***Beginning  Linux Programming 4th Edition*** - 15.2.9实例

client

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>

int main()
{
    int sockfd;
    int len;
    struct sockaddr_in6 address;
    int result;
    char ch = 'A';

    sockfd = socket(AF_INET6, SOCK_STREAM, 0);

    address.sin6_family = AF_INET6;
    address.sin6_port = htons(10001);
    if (inet_pton(AF_INET6, "::1", &address.sin6_addr) != 1)
    {
        fprintf(stderr, "inet_pton(2) error\n");
        exit(1);
    }
    len = sizeof(address);

    result = connect(sockfd, (struct sockaddr *)&address, len);
    if (result == -1)
    {
        perror("open: client");
        exit(1);
    }

    send(sockfd, &ch, 1, 0);
    recv(sockfd, &ch, 1, 0);
    printf("char from server = %c\n", ch);
    close(sockfd);

    return 0;
}
```

server

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>

int main()
{
    int server_sockfd, client_sockfd;
    int server_len, client_len;
    struct sockaddr_in6 server_address;
    struct sockaddr_in6 client_address;

    server_sockfd = socket(AF_INET6, SOCK_STREAM, 0);
    server_address.sin6_family = AF_INET6;
    server_address.sin6_addr = in6addr_any;
    server_address.sin6_port = htons(10001);
    server_len = sizeof(server_address);
    bind(server_sockfd, (struct sockaddr *)&server_address, server_len);

    listen(server_sockfd, 5);
    while (1) 
    {
        char ch;
        printf("server waiting...\n");

        client_len = sizeof(client_address);
        client_sockfd = accept(server_sockfd, 
                               (struct sockaddr *)&client_address, (socklen_t *)&client_len);

        recv(client_sockfd, &ch, 1, 0);
        ch++;
        send(client_sockfd, &ch, 1, 0);
        close(client_sockfd);
    }

    return 0;
}
```

这里也只是将`struct sockaddr_in`换成`struct sockaddr_in6`就可以实现IPv6，它的定义如下

```c
struct sockaddr_in6 {
	sa_family_t     sin6_family;   /* AF_INET6 */
	in_port_t       sin6_port;     /* port number */
	uint32_t        sin6_flowinfo; /* IPv6 flow information */
	struct in6_addr sin6_addr;     /* IPv6 address */
	uint32_t        sin6_scope_id; /* Scope ID (new in 2.4) */
};

struct in6_addr {
	unsigned char   s6_addr[16];   /* IPv6 address */
};
```

同时这里不使用`read(2)`和`write(2)`来接收和发送数据，而使用专门的`send(2)`和`recv(2)`函数（[send(2)](http://man7.org/linux/man-pages/man2/send.2.html)、[recv(2)](http://man7.org/linux/man-pages/man2/recv.2.html)）



因为IPv4和IPv6的demo涉及到网路通信，而主机一般是使用大端模式（Big-endian）存储数据，而网络则是以小端（Little-endian）模式进行数据传输的，所以这几个demo中在设置地址和端口是都进行了转化（[htonl(3)](http://man7.org/linux/man-pages/man3/htonl.3p.html)、[htons(3)](http://man7.org/linux/man-pages/man3/htonl.3p.html)、[ntohl(3)](http://man7.org/linux/man-pages/man3/ntohl.3p.html)、[ntohs(3)](http://man7.org/linux/man-pages/man3/ntohl.3p.html)、[inet_addr(3)](http://man7.org/linux/man-pages/man3/inet_addr.3p.html)、[inet_ntop(3)](http://man7.org/linux/man-pages/man3/inet_ntop.3.html)、[inet_pton(3)](http://man7.org/linux/man-pages/man3/inet_pton.3.html)）



### 数据报

client

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main(int argc, char *argv[])
{
    int sockfd;
    struct sockaddr_in address;
    
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    address.sin_family = AF_INET;
    address.sin_port = htons(10002);
    address.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (argc == 1)
    {
        sendto(sockfd, argv[0], strlen(argv[0]), 0, 
               (struct sockaddr *)&address, sizeof(address));
    }
    else
    {
        sendto(sockfd, argv[1], strlen(argv[1]), 0, 
               (struct sockaddr *)&address, sizeof(address));
    }

    return 0;
}
```

server

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <pthread.h>

int main()
{
    int sockfd;
    int socket_len;
    char buffer[100];
    char recvip[20];
    struct sockaddr_in address;
    struct sockaddr_in listen_addr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    listen_addr.sin_family = AF_INET;
    listen_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    listen_addr.sin_port = htons(10002);
    bind(sockfd, (struct sockaddr *)&listen_addr, sizeof(listen_addr));

    while (1) 
    {
        printf("waiting...\n");
        recvfrom(sockfd, (void *)buffer, 100, 0,
                 (struct sockaddr *)&address, &socket_len);

        inet_ntop(AF_INET, &address, recvip, 20);
        printf("from %s:%d receive data: %s\n", 
              recvip, ntohs(address.sin_port), buffer);
    }

    return 0;
}
```

使用UDP进行数据传输不用像TCP那样要先进行连接，可以直接通过`sendto(2)`和`recvfrom(2)`发送和接收数据，服务器也不用`accept(2)`等待连接（[sendto(2)](http://man7.org/linux/man-pages/man2/send.2.html)、[recvfrom(2)](http://man7.org/linux/man-pages/man2/recv.2.html)）



### 网络信息

获取主机信息

```c
#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>

int main()
{
    struct hostent *hostentry;
    char *p;
    int i;

    if ((hostentry = gethostbyname("www.baidu.com")) == NULL)
    {
        fprintf(stderr, "gethostbyname(3) error\n");
        exit(1);
    }

    printf("official name: %s\n", hostentry->h_name);
    printf("alias list:\n");
    i = 0;
    while ((p = hostentry->h_aliases[i++]) != NULL)
    {
        printf("\t> %s\n", p);
    }
    printf("address list:\n");
    i = 0;
    while ((p = hostentry->h_addr_list[i++]) != NULL)
    {
        printf("\t> %s\n", p);
    }

    return 0;
}
```

[gethostbyname(3)](http://man7.org/linux/man-pages/man3/gethostbyname.3.html)



获取协议信息

```c
#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>

int main()
{
    struct protoent *protoentry;
    char *p;
    int i;

    setprotoent(0);
    while ((protoentry = getprotoent()) != NULL)
    {
        printf("official protocol name: %s\n", protoentry->p_name);
        printf("protocol number: %d\n", protoentry->p_proto);
        printf("alias list:\n");
        i = 0;
        while ((p = protoentry->p_aliases[i++]) != NULL)
        {
            printf("\t> %s\n", p);
        }
        printf("\n");
    }

    return 0;
}
```

[getprotobynam(3)](http://man7.org/linux/man-pages/man3/getprotoent.3.html)



获取服务信息

```c
#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>

int main()
{
    struct servent *serverentry;
    char *p;
    int i;

    if ((serverentry = getservbyname("http", "tcp")) == NULL)
    {
        fprintf(stderr, "getservbyname(3) error\n");
        exit(1);
    }

    printf("offical service name: %s\n", serverentry->s_name);
    printf("port number: %d\n", serverentry->s_port);
    printf("alias list:\n");
    while ((p = serverentry->s_aliases[i++]) != NULL)
    {
        printf("\t> %s\n", p);
    }
    printf("protocol to use: %s\n", serverentry->s_proto);

    return 0;
}
```

[getservbyname(3)](http://man7.org/linux/man-pages/man3/getservbyname.3.html)



这三类函数的命名都类似

