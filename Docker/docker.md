# Docker常用命令



## 参考

> [W3Cschool](https://www.w3cschool.cn/docker/docker-tutorial.html)
>
> [RUNOOB](https://www.runoob.com/docker/docker-tutorial.html)
>
> [Docker命令](https://docs.docker.com/reference/)
>
> [Docker Get Started](https://docs.docker.com/get-started/)
>
> [Docker Install](https://docs.docker.com/engine/install/)
>
> [Dockerfile参考](https://docs.docker.com/engine/reference/builder/)



## 安装

如果之前有安装过docker，那么先卸载旧版的docker

```shell
$ yum remove docker \
                docker-client \
                docker-client-latest \
                docker-common \
                docker-latest \
                docker-latest-logrotate \
                docker-logrotate \
                docker-engine
```

然后设置repository

```shell
$ yum install -y yum-utils
$ yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

最后安装

```shell
$ yum install docker-ce docker-ce-cli containerd.io
```

启动docker并且设置开机启动

```shell
$ systemctl enable docker.service
$ systemctl start docker.service
```

验证

```shell
$ docker version 		# 显示版本信息
$ docker info			# 显示docker的配置信息
$ docker help			# 显示docker的帮助
```



## 导出/导入镜像

```shell
# 导出镜像到tar.gz
$ docker save nginx >/tmp/nginx.tar.gz
# 从tar.gz导入镜像
$ docker load < /tmp/nginx.tar.gz
```



## docker基础

运行一个容器：

```shell
$ docker run ubuntu:15.10 /bin/echo "hello world"
```

run是运行一个容器

ubuntu:15.10是制定运行的镜像，首先会从本地查找镜像是否存在，如果不存在就会去docker镜像站下载

/bin/echo "Hello World"在启动容器里执行的命令



```shell
$ docker run -i -t ubuntu:15.10 /bin/bash
Dc0050c79503:/# 
```

-t 在新容器内制定一个伪终端或终端

-i 允许你对容器内的标准输入(STDIN)进行交互 



 使用以下命令创建一个以进程方式运行的容器 

```shell
$ docker run -d ubuntu:15.10 /bin/sh -c "while true; do echo hello world; sleep 1; done"
2b1b7a428627c51ab8810d541d759f072b4fc75487eed05812646b8534a2fe63

// 循环打印Hello World
// 后面的hash是容器ID
```

```shell
$ docker ps           //加上-a参数是列出所有容器（包括没有运行的）
```

```shell
$ docker logs 2b1b7a428627c5         //可以是容器ID或者是容器名称（docker ps输出的NAMES列）
```

在后台运行ubuntu容器，输出一个容器ID，在终端看不到输出Hello World

第二条命令查看当前运行的容器

第三条命令查看指定容器ID的标准输出



```shell
$ docker stop 2b1b7a428627c5         //可以是容器ID或者是容器名称（docker ps输出的NAMES列）
```

该命令用来停止一个容器



## 使用容器

用Python Flask运行web应用：

```shell
$ docker run -d -P training/webapp python app.py
```

-d 表示让容器在后台运行

-P 将容器内部使用的网络端口映射到物理主机上

```
0.0.0.0:32768->5000/tcp
```

这个默认会把（Flask使用的5000端口隐射到32768端口上）



```shell
$ docker run -d -p 5000:5000 training/webapp python app.py
```

将容器内的5000端口映射到本机的5000端口

-p <本地端口>:<容器的服务端口>

格式：

```
ip:hostPort:containerPort
ip::containerPort
hostPort:containerPort
containerPort
```

-i 以交互式方式运行容器，通常与-t一起使用

-t 为容器重新分配一个伪输入终端，通常与-i一同使用

-P 随机端口映射

--name="容器的名字" 为容器指定一个名称

-d 后台运行容器，并返回容器ID，也即启动守护容器



启动一个centos7容器并且以交互式进入

```shell
[root@localhost etc]# docker run -it centos:7
[root@5d4e2f820c4d /]#
```



```shell
$ docker port <容器ID或者容器名>
```

查看指定容器的端口映射情况



```shell
$ docker logs -f -t --tail <容器ID或者容器名>
$ docker top <容器ID或者容器名>
$ docker inspect <容器ID或者容器名,数据卷等任何docker对象>
```

-f 的作用是让docker logs使用像tail -f的命令输出方式，也就是跟随着日志更新而更新，-t显示时间戳，--tail从日志末尾显示多少行，默认为“all”，也就是全部行

第二条命令用来查看容器内运行的进程

第三条命令查看docker的容器的底层信息，返回的是JSON数据格式



停用和启用容器：

```shell
$ docker start <容器名或者容器ID>
$ docker stop <容器名或者容器ID>
$ docker kill <容器名或者容器ID>	# 强制停止容器
```



退出容器（在容器里面）：

* ~~exit 容器停止并退出~~
* ~~ctrl + P + Q 容器不停止退出~~



移除容器：

```shell
$ docker rm <容器名或者容器ID>
```

移除容器（强制）
```shell
$ docker rm -f <容器名或者容器ID>
```

强制移除所有容器：

```shell
$ docker rm -f $(docker ps -a -q)
```



进入容器：

```shell
$ docker exec -it <容器名或ID> /bin/bash		# 这对容器执行命令并返回结果，-it则以交互式产生一个伪终端，/bin/bash则执行一个shell
$ docker attach <容器名或者容器ID>				# 直接进入容器设置好的目录
```

区别：

* attach：直接进入容器启动命令的终端，不会启动新进程
* exec：是在容器中打开新的终端，并且可以启动新进程



不进入容器并并在容器中执行命令

```shell
[root@localhost ~]# docker exec -t hopeful_lehmann ls -a
.   .dockerenv         bin  etc   lib    media  opt   root  sbin  sys  usr
..  anaconda-post.log  dev  home  lib64  mnt    proc  run   srv   tmp  var
```



修改容器名

```shell
$ docker rename <旧的容器名称> <新的容器名称>
```



查看正在运行的容器：

```shell
$ docker ps
```

-a 显示所有容器，默认只显示正在运行的

-n [int] 显示最后创建的几个容器，默认-1，就是显示全部

-q 只显示容器ID

-l 显示最后创建的容器



拷贝容器中的数据到宿主机上/拷贝宿主机的数据到容器中：

```shell
$ docker cp <容器ID>:<容器路径> <宿主机目录>
$ docker cp <宿主机目录> <容器ID>:<容器路径>
```



## 使用镜像

查看本地镜像：

```shell
$ docker images
$ docker images --all		# 显示所有镜像
$ docker images -q			# 只显示镜像的ID
$ docker images --digests	# 显示镜像的数字签名
$ docker images --no-trunc	# 输出详细信息,不截断内容
```



运行一个镜像（加上TAG）

```shell
$ docker run -t -i ubuntu:15.10 /bin/bash
$ docker run -t -i ubuntu:14.04 /bin/bash  
```



下载镜像：

```shell
$ docker pull ubuntu:13.10
```

Docker 镜像站（Docker Hub）：https://hub.docker.com/



上传镜像：

```shell
$ docker push <你自己的镜像>
```



搜索镜像：

```shell
$ docker search httpd				# 搜索httpd的镜像
$ docker search --no-trunc nginx	# 索索nginx镜像并显示详细的DESCRIPTION信息
$ docker search --limit 10 nginx	# 搜索nginx镜像并只显示前10个
```



## 创建镜像

### 更新镜像：

1. 首先用镜像创建一个容器：`# docker run -t -i ubuntu:15.10 /bin/bash`
2. 在这个ubuntu里面更新（apt update）：`# apt update`
3. 然后生成新的镜像（docker commit -m="说明信息" -a="镜像作者" <容器ID> <生成的路径>）：`# docker commit -m="has update" -a="ikun" 34a46bc8d97 test/test:v1`

commit 提交一个容器副本使之称为一个新的镜像

```shell
$ docker commit -m="has update" -a="ikun" 34a46bc8d97 test/test:v1`
```

-m 提交信息

-a 镜像作者



### 构建镜像：

1. 首先创建Dockerfile文件，里面包含docker如何创建镜像

   ```dockerfile
   FROM	centos:6.7
   MAINTAINER	Fisher "fisher@sudops.com"
   
   RUN		/bin/echo 'root:123456' | chpasswd
   RUN 	/useradd youj
   RUN 	/bin/echo 'youj:123456' | chpasswd
   RUN 	/bin/echo -e "LANG=\"en_US.UTF-8\"" &gt; /etc/default/local
   EXPOSE	22
   EXPOSE	80
   CMD		/usr/sbin/sshd -D
   
   // 第一个命令是在镜像上创建一个新的层，每一个指令的前缀都必须是大写的
   // 第一条FROM，指定使用哪个镜像
   // RUN命令告诉docker在镜像内执行的命令，安装了什么
   ```

2. 然后使用Dockerfile文件，通过docker build命令构建一个镜像： 

   ```
   # docker build -t youj/centos:6.7 .         //-t是目标镜像的名称，（.）是创建的路径
   ```



### 设置镜像标签：

```shell
$ docker tag f1a24979af43 youj/centos:6.7
```



### 删除镜像：

```shell
$ docker rmi -f <镜像名...>
$ docker rmi -f $(docker images -qa)	# 删除所有镜像
```

-f 表示强制删除，同时删除容器



## 连接容器

网路端口映射：

```shell
$ docker run -d -P training/webapp python app.py
$ docker run -d -p 5000:5000 training/webapp python app.py
$ docker run -d -p 127.0.0.1:5001:5002 training/webapp python app.py
$ docker run -d -p 127.0.0.1:5000:5000/udp training/webapp python app.py
```

绑定主机端口到容器端口，也可以绑定地址和协议

-P：使容器内部端口随机映射到主机的高端口

-p：使容器内部端口绑定到指定的主机端口



查看端口绑定情况：

```shell
$ docker port adoring_stonebraker 5001
```



容器命名：

```shell
$ docker run -d -p 8000:5000 --name mysite training/webapp python app.py
```



删除容器：

```shell
$ docker rm <容器名或容器ID>
```



复制容器目录：

```shell
$ docker cp 容器ID:容器目录 主机目录
```



终止容器进程：

```shell
$ docker kill -s <信号> <容器ID或者容器名>
```

-s指定一个kill的信号，默认KILL（差不多就是kill -l列出来的信号（去掉SIG前缀））



## DockerFile

```dockerfile
FROM	centos:6.7
MAINTAINER	Fisher "fisher@sudops.com"

RUN		/bin/echo 'root:123456' | chpasswd
RUN 	/useradd youj
RUN 	/bin/echo 'youj:123456' | chpasswd
RUN 	/bin/echo -e "LANG=\"en_US.UTF-8\"" &gt; /etc/default/local
EXPOSE	22
EXPOSE	80
CMD		/usr/sbin/sshd -D

// 第一个命令是在镜像上创建一个新的层，每一个指令的前缀都必须是大写的
// 第一条FROM，指定使用哪个镜像
// RUN命令告诉docker在镜像内执行的命令，安装了什么
// CMD是运行一条命令
```

```dockerfile
FROM 			ubuntu
MAINTAINER		xbf
RUN				apt-get update
RUN				apt-get install -y nginx
COPY 			index.html /var/www/html
ENTRYPOINT		["/usr/sbin/nginx", "-g", "daemon off;"]
EXPOSE			80

// FROM是指向基础镜像
// MAINTAINER是镜像作者
// RUN是执行的命令
// COPY是考本本地的文件到容器里面
// ENTRYPOINT是入口点，使用数组参数，将nginx在前台执行，而不是守护进程
// EXPOSE是开放一个端口
```



DockerFile语法：

| 命令       | 用途                                                         |
| ---------- | ------------------------------------------------------------ |
| FROM       | 基础镜像，当前新镜像是基于哪个镜像                           |
| RUN        | 容器构建时需要运行的命令                                     |
| COPY       | 类似ADD，拷贝文件和目录到镜像中，格式：`COPY src dst` 或者 `COPY ["src", "dst"]` |
| ADD        | 将宿主主机目录下的文件拷贝进镜像且ADD命令会自动处理URL和解压tar压缩包 |
| CMD        | 指定一个容器启动时要运行的命令。Dockerfile中可以有多个CMD命令，**但只有最后一个生效**，CMD会被docker run之后的参数替换（也就是相当于他会在Dockerfile最后加上一个 `CMD 你docker run 后的命令`），格式：`CMD <命令>` 或者 `CMD ["命令", "参数1", "参数2", ...]`。`CMD ["参数1", "参数2", ...]` 在指定 `ENTRYPOINT` 指令后 `CMD` 指定具体参数 |
| EXPOSE     | 当前容器对外暴露出的端口                                     |
| WORKDIR    | 指定在容器创建后，终端登录进来的工作目录                     |
| MAINTAINER | 镜像的维护者的姓名和邮箱                                     |
| ENV        | 用来在构建镜像过程中设置环境变量                             |
| ENTRYPOINT | 指定一个容器启动时要运行的命令。ENTRYPOINT和CMD的目的一样，但ENTRYPOINT会把docker run后面的参数追加 |
| USER       | 指定用户                                                     |
| VOLUME     | 容器数据卷，用于数据保存和持久化工作                         |
| ONBUILD | 当构建一个被继承的Dockerfile时运行的命令，父镜像在被子镜像继承后父镜像的onbuild会被触发 |
从Dockerfile构建Docker镜像

```shell
$ docker build -f /path/to/a/Dockerfile .
$ docker build -f /path/to/a/Dockerfile -t aa/bb:tag .	# 构建image并使用-t后的名字和tag
```

查看镜像的变更历史：

```
[root@localhost ~]# docker history mycentos:v1 
IMAGE               CREATED             CREATED BY                                      SIZE                COMMENT
d1c1907306d4        5 minutes ago       /bin/sh -c #(nop)  CMD ["/bin/sh" "-c" "/bin…   0B                  
202ad485c3f8        5 minutes ago       /bin/sh -c #(nop)  CMD ["/bin/sh" "-c" "echo…   0B                  
70c47798b8f6        5 minutes ago       /bin/sh -c #(nop)  CMD ["/bin/sh" "-c" "echo…   0B                  
98d957c520ea        5 minutes ago       /bin/sh -c #(nop)  EXPOSE 80                    0B                  
520550ccb8e8        5 minutes ago       /bin/sh -c yum -y install net-tools             92.3MB              
de5be8ff7aa3        5 minutes ago       /bin/sh -c yum -y install vim                   147MB               
1278995a177c        6 minutes ago       /bin/sh -c #(nop) WORKDIR /tmp                  0B                  
26122d69c95b        6 minutes ago       /bin/sh -c #(nop)  ENV LOGIN_PATH=/tmp          0B                  
d0619cd97171        6 minutes ago       /bin/sh -c #(nop)  MAINTAINER ikun0000<20662…   0B                  
7e6257c9f8d8        3 weeks ago         /bin/sh -c #(nop)  CMD ["/bin/bash"]            0B                  
<missing>           3 weeks ago         /bin/sh -c #(nop)  LABEL org.label-schema.sc…   0B                  
<missing>           3 weeks ago         /bin/sh -c #(nop) ADD file:61908381d3142ffba…   203MB
```





## 数据卷

```shell
$ docker run -d -v /usr/share/nginx/html nginx		# 目录使rw
$ docker run -d -v /usr/share/nginx/html:rw nginx	# 容器目录读写
$ docker run -d -v /usr/share/nginx/html:ro nginx	# 容器目录只读（容器内只读，宿主机可读可写）
```

运行nginx镜像并把容器内部/usr/share/nginx/html挂在到宿主机（使用docker inspect 可以查看（Mounts）），也可以 -v 宿主目录或者volume名:容器目录:权限，最后带上对容器的权限

查看数据卷挂载

```shell
$ docker inspect mycentos
[
    {
        "Id": "a9417e53d442dd5b052f1119f247ca583f11ddc00ca634f4bf10ef9ae59aa07c",
        "Created": "2020-09-01T06:41:55.256481975Z",
        "Path": "/bin/bash",
        "Args": [],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 1942,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2020-09-01T06:41:55.506891591Z",
            "FinishedAt": "0001-01-01T00:00:00Z"
        },
        "Image": "sha256:7e6257c9f8d8d4cdff5e155f196d67150b871bbe8c02761026f803a704acb3e9",
        "ResolvConfPath": "/var/lib/docker/containers/a9417e53d442dd5b052f1119f247ca583f11ddc00ca634f4bf10ef9ae59aa07c/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/a9417e53d442dd5b052f1119f247ca583f11ddc00ca634f4bf10ef9ae59aa07c/hostname",
        "HostsPath": "/var/lib/docker/containers/a9417e53d442dd5b052f1119f247ca583f11ddc00ca634f4bf10ef9ae59aa07c/hosts",
        "LogPath": "/var/lib/docker/containers/a9417e53d442dd5b052f1119f247ca583f11ddc00ca634f4bf10ef9ae59aa07c/a9417e53d442dd5b052f1119f247ca583f11ddc00ca634f4bf10ef9ae59aa07c-json.log",
        "Name": "/mycentos",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "",
        "ExecIDs": null,
        "HostConfig": {
            "Binds": [
                "/test:/test"
            ],
...
```



创建容器卷：

```
[root@localhost myDocker]# docker volume create --name v1
v1
```

--name 指定容器卷名称



查看所有容器数据卷映射：

```
[root@localhost myDocker]# docker volume ls
DRIVER              VOLUME NAME
local               03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3
local               55bb9e73da146cbdcc11435c4dcf82cc3abaf4aa725bad66c4dce6dac4d0eb45
root@localhost myDocker]# docker volume inspect 03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3
[
    {
        "CreatedAt": "2020-09-01T15:20:58+08:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3/_data",
        "Name": "03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3",
        "Options": null,
        "Scope": "local"
    }
]
[root@localhost myDocker]# docker volume inspect 03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3
[
    {
        "CreatedAt": "2020-09-01T15:20:58+08:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3/_data",
        "Name": "03c9c0404cb6246f7ba72116641279bcd53df80d14055f4bfd268c67412171d3",
        "Options": null,
        "Scope": "local"
    }
]
```



删除数据卷：

```shell
$ docker volume rm v1
```

-f 强制删除



持久数据容器：

```shell
$ mkdir data
$ docker create -v $PWD/data:/var/mydata --name data_container ubuntu
$ docker run -it --volume-from data_container ubuntu /bin/bash
```

首先创建目录

然后创建一个数据容器，并且挂在本地目录

最后创建的容器使用--volume-from指定数据容器



显示容器中文件的更改情况：

```shell
$ docker diff <容器名或容器ID>
```

| Symbol | Description                     |
| :----- | :------------------------------ |
| `A`    | A file or directory was added   |
| `D`    | A file or directory was deleted |
| `C`    | A file or directory was changed |



从别的镜像上挂载数据卷：

```shell
$ docker run -dit --name dc01 -v /data1:data1 -v /data2:/data2 test/test:v1
$ docker run -dit --name dc02 --volumes-from dc01 test/test:v1
$ docker run -dit --name dc03 --volumes-from dc01 /data1:data1 -v /data2:/data2 test/test:v1
```

dc02和dc03的数据卷和dc01挂在到宿主机的目录一样，要求必须是同一个镜像生成的容器，宿主的数据卷可以实现在多个实例中共享文件，删除一个实例不会影响到共享，除非所有实例都被删除



## Docker网络

获取Docker网路配置

```shell
[root@localhost ~]$ docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
262ed25c6361        bridge              bridge              local
d56141bf8757        host                host                local
457edc4bceac        none                null                local
```

DRIVER

* bridge：bridge网路会在物理机网卡上有一个docker 0的虚拟网卡，作为docker的网关，使用bridge就是连接到这块虚拟网卡，通过这块虚拟网卡连接物理网卡访问外网，类似于vmware的NAT模式
* host：使用host模式后容器就会和宿主机共用一块网卡，类似于vmware的桥接模式，直接桥接到物理网卡
* none：此时所有网路都得自己配置ip，网卡等，类似于vmware的自定义



创建一个桥接模式的网络

```shell
$ docker network create test_net
```

什么都不指定默认创建一个bridge模式的网路

在创建容器时使用`--network`参数指定容器连接的网路



## 问题

```shell
[root@localhost ~]# docker exec -it /bin/bash some-mysql
Error response from daemon: page not found
```

修改`/etc/docker/daemon.json`

```json
{
    "registry-mirrors": ["https://reg-mirror.giniu.com"],
    "insecure-registries": ["myregistry.example.com:5000"]
}
```

重启docker

```shell
$ systemctl restart docker
```

