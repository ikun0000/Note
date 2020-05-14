# Docker常用命令



## 参考

> [W3Cschool](https://www.w3cschool.cn/docker/docker-tutorial.html)
>
> [RUNOOB](https://www.runoob.com/docker/docker-tutorial.html)



#### docker基础

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



#### 使用容器

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



```shell
$ docker port <容器ID或者容器名>
```

查看指定容器的端口映射情况



```shell
$ docker logs -f <容器ID或者容器名>
$ docker top <容器ID或者容器名>
$ docker inspect <容器ID或者容器名>
```

-f 的作用是让docker logs使用像tail -f的命令输出方式

第二条命令用来查看容器内运行的进程

第三条命令查看docker的容器的底层信息，返回的是JSON数据格式



停用和启用容器：

```shell
$ docker start <容器名或者容器ID>
$ docker stop <容器名或者容器ID>
```



移除容器：

```shell
$ docker rm <容器名或者容器ID>
```

移除容器（强制）
```shell
$ docker rm -f <容器名或者容器ID>
```



进入容器：

```shell
$ docker exec -it <容器名或ID>  /bin/bash
```



修改容器名

```shell
$ docker rename <旧的容器名称> <新的容器名称>
```



#### 使用镜像

查看本地镜像：

```shell
$ docker images
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
$ docker search httpd
```



##### 创建镜像

###### 更新镜像：

1. 首先用镜像创建一个容器：`# docker run -t -i ubuntu:15.10 /bin/bash`
2. 在这个ubuntu里面更新（apt update）：`# apt update`
3. 然后生成新的镜像（docker commit -m="说明信息" -a="镜像作者" <容器ID> <生成的路径>）：`# docker commit -m="has update" -a="ikun" 34a46bc8d97 test/test:v1`



###### 构建镜像：

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



##### 设置镜像标签：

```shell
$ docker tag f1a24979af43 youj/centos:6.7
```



##### 删除镜像：

```shell
$ docker rmi -f <镜像名>
```

-f 表示强制删除，同时删除容器



#### 连接容器

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



##### DockerFile

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
| COPY       | 类似AD，拷贝文件和目录到镜像中                               |
| ADD        | 将宿主主机目录下的文件拷贝进镜像且ADD命令会自动处理RUL和解压tar压缩包 |
| CMD        | 指定一个容器启动时要运行的命令。Dockerfile中可以有多个CMD命令，但只有最后一个生效，CMD会被docker run之后的参数替换 |
| EXPOSE     | 当前容器对外暴露出的端口                                     |
| WORKDIR    | 指定在容器创建后，终端登录进来的工作目录                     |
| MAINTAINER | 镜像的维护者的姓名和邮箱                                     |
| ENV        | 用来在构建镜像过程中设置环境变量                             |
| ENTRYPOINT | 指定一个容器启动时要运行的命令。ENTRYPOINT和CMD的目的一样，但ENTRYPOINT会把docker run后面的参数追加 |
| USER       | 指定用户                                                     |
| VOLUME     | 容器数据卷，用于数据保存和持久化工作                         |
| ONBUILD | 当构建一个被继承的Dockerfile时运行的命令，父镜像在被子镜像继承后父镜像的onbuild会被触发 |
##### Volume

```
# docker run -d -v /usr/share/nginx/html nginx
# docker run -d -v /usr/share/nginx/html:ro nginx
```

运行nginx镜像并把容器内部/usr/share/nginx/html挂在到宿主机（使用docker inspect 可以查看（Mounts）），也可以 -v 宿主目录:容器目录，最后带上对容器的权限



持久数据容器：

```shell
$ mkdir data
$ docker create -v $PWD/data:/var/mydata --name data_container ubuntu
$ docker run -it --volume-from data_container ubuntu /bin/bash
```

首先创建目录

然后创建一个数据容器，并且挂在本地目录

最后创建的容器使用--volume-from指定数据容器







# 问题

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

