# nginx使用笔记

## 安装

*  安装[pcre]( http://www.pcre.org/ )（需要g++，源码安装）
* 安装openssl`yum -y install make zlib zlib-devel gcc-c++ libtool openssl openssl-dev`
* 安装zlib
* 安装[nginx]( http://nginx.org/ )（源码安装）



#### 解决问题

```
[root@localhost sbin]# ./nginx
./nginx: error while loading shared libraries: libpcre.so.0: cannot open shared object file: No such file or directory

[root@localhost sbin]# find / -name libpcre.so.*
/root/pcre-8.00/.libs/libpcre.so.0.0.1
/root/pcre-8.00/.libs/libpcre.so.0
/usr/lib64/libpcre.so.1
/usr/lib64/libpcre.so.1.2.0
/usr/local/lib/libpcre.so.0.0.1
/usr/local/lib/libpcre.so.0

[root@localhost sbin]# ln -s /usr/local/lib/libpcre.so.0 /lib64/
```



## nginx命令（在nginx安装目录的sbin下）

显示帮助

```
# ./nginx -h
```

查看版本号

```
# ./nginx -v
```

启动nginx

```
# ./nginx
# ps -ef | grep nginx
```

关闭nginx

```
# ./nginx -s stop
```

重新加载nginx

```
# ./nginx -s reload
```



## 配置文件（/usr/local/conf/nginx.conf）

```

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
            autoindex on;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```

#### 全局块

设置一些影响nginx服务器整体运行的配置指令

例如：配置并发，值越大，可以支持并发处理量也越多

```
worker_processes  1;
```

#### events块

影响nginx服务器与用户网路的链接

例如：支持的最大连接数

```
worker_connections  1024;
```

#### http块

反向代理，负载均衡，动静分离都在这里配置

> http全局块：
>
> http全局块配置的指令包括文件引入，MIME-TYPE定义，日志自定义，链接超时时间，单链接请求数上限等

> server块
>
> server块和虚拟主机有关
>
> 每个http块可以包含多个server块，每个server块相当于一个虚拟主机，每个server块也分为全局server块，以及可以包含多个location块
>
> 通常配置虚拟主机监听配置和本虚拟主机名或IP配置
>
> > location块
> >
> > location块是收到请求字符串进行匹配对特定请求进行处理，地址定向，数据缓存和应答控制功能





## 配置反向代理

实现访问 www.abc.com 代理到 http://www.baidu.com

修改server块

```
server {
        listen       80;
        server_name  10.10.10.246;


        location / {
            proxy_pass http://www.baidu.com;
            # root   html;
            # index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
```

> server_name修改为本机的IP或者域名
>
> 在location块中配置proxy_pass <代理到的域名>实现反向代理



实现访问 http://www.abc.com/baidu/ 跳转到 http://www.baidu.com，访问 http://www.abc.com/bing/ 跳转到 http://cn.bing.com/ 

```
server {
        listen       80;
        server_name  10.10.10.246;

        location ~ /baidu/ {
            proxy_pass http://www.baidu.com;
            # root   html;
            # index  index.html index.htm;
        }

        location ~ /bing/ {
            proxy_pass http://cn.bing.com;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
```

> 使用location ~ 开启正则表达式匹配
>
> 
>
> location = : 用于不含正则表达式的uri前，要求请求字符串和url严格匹配，如果匹配成功，就停止继续向下搜索并立即处理请求
>
> location ~ : 用于表示uri包含正则表达式，并且区分大小写
>
> location ~* : 用于表示uri包含正则表达式，并且不区分大小写
>
> location ^~ : 用于不含正则表达式的uri前，要求nginx服务器找到标识url和请求字符串匹配度最高的location后，立即使用此location处理请求，而不再使用location块中的正则uri和请求字符串做匹配
>
> 
>
> 如果uri中包含正则表达式，则必须有~或者~*标识





## 配置负载均衡

实现访问80分摊到8080和8081

```
http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;

    upstream myserver {
        server 10.10.10.246:8080 weight=1;
        server 10.10.10.246:8081 weight=1;
    }

    server {
        listen       80;
        server_name  10.10.10.246;

        location / {
            proxy_pass http://myserver;
            # proxy_connect_timeout 10;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
    
    server {
        listen       8080;
        server_name  10.10.10.246;

        location / {
            root aaa;
            index index.html;
        }
    }

    server {
        listen       8081;
        server_name  10.10.10.246;

        location / {
            root bbb;
            index index.html;
        }
    }
}
```

> upstream 用于配置服务器列表
>
> 在 location 里面配置 proxy_pass 为之前设置的 upstream 名称即可
>
> 
>
> 负载均衡策略：
>
> 轮询（默认）：
>
> 么个服务器按时间顺序逐一分配客户端，如果服务器down掉，能自动移除
>
> ```
> upstream myserver {
>  server 10.10.10.246:8080;
>  server 10.10.10.246:8081;
> }
> ```
>
> 
>
> weight：
>
> weight代表权，默认为1，权重越高被分配的客户端越多
>
> ```
> upstream myserver {
>  server 10.10.10.246:8080 weight=1;
>  server 10.10.10.246:8081 weight=1;
> }
> ```
>
> 
>
> ip_hash：
>
> 每个请求按访问IP的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题
>
> ```
> upstream myserver {
> ip_hash;
>  server 10.10.10.246:8080;
>  server 10.10.10.246:8081;
> }
> ```
>
> 
>
> fair（第三方）：
>
> 按后端服务器响应时间来分配请求，响应时间短的优先分配
>
> ```
> upstream myserver {
>  server 10.10.10.246:8080;
>  server 10.10.10.246:8081;
>  fair;
> }
> ```





## 配置动静分离

实现访问txt文件访问http://127.0.0.1:81，访问php文件跳转到apache的http://127.0.0.1:8000

```
    server {
        listen       80;
        server_name  localhost;

        location ~ \.txt$ {
            proxy_pass http://127.0.0.1:81;
        }

        location ~ \.php$ {
            proxy_pass http://127.0.0.1:8000;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
```