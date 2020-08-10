# Redis安装与配置



## Docker安装Redis

下载Redis镜像

```shell
$ docker pull redis
```

安装

```shell
$ docker run -dit --name myredis -p 6379:6379 redis
```

检测安装，进入容器中

```shell
$ docker exec -it myredis /bin/bash
```

连接Redis并测试

```shell
$ redis-cli
127.0.0.1:6379> PING
PONG
```

测试成功



## Redis是什么？

Redis是一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API。和Memcached类似，它支持存储的value类型相对更多，包括string(字符串)、list(链表)、set(集合)、zset(sorted set --有序集合)和hash（哈希类型）。这些数据类型都支持push/pop、add/remove及取交集并集和差集及更丰富的操作，而且这些操作都是原子性的。在此基础上，redis支持各种不同方式的排序。与memcached一样，为了保证效率，数据都是缓存在内存中。区别的是redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了master-slave(主从)同步。



### Redis性能

测试完成了50个并发执行100000个请求。
设置和获取的值是一个256字节字符串。
Linux box是运行Linux 2.6,这是X3320 Xeon 2.5 ghz。
文本执行使用loopback接口(127.0.0.1)。
结果:读的速度是110000次/s,写的速度是81000次/s 。



### Redis持久化

redis使用了两种文件格式：全量数据（RDB）和增量请求（AOF）。

全量数据格式是把内存中的数据写入磁盘，便于下次读取文件进行加载；
增量请求文件则是把内存中的数据序列化为操作请求，用于读取文件进行replay得到数据，序列化的操作包括SET、RPUSH、SADD、ZADD。

redis的存储分为内存存储、磁盘存储和log文件三部分，配置文件中有三个参数对其进行配置。
save seconds updates，save配置，指出在多长时间内，有多少次更新操作，就将数据同步到数据文件。这个可以多个条件配合，比如默认配置文件中的设置，就设置了三个条件。
appendonly yes/no ，appendonly配置，指出是否在每次更新操作后进行日志记录，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为redis本身同步数据文件是按上面的save条件来同步的，所以有的数据会在一段时间内只存在于内存中。



## Redis服务器

如果直接下载Redis的二进制压缩包那么会在bin目录下有个 `redis-server` 的命令，这个就是Redis服务器的启动程序。他的格式为

```shell
$ ./redis-server [启动时使用的配置文件] [参数]
```

如果没有指定启动时的配置文件就会使用默认的配置文件

参数：

* `--port <port>` 启动的端口
* `--loglevel` 日志级别



## Redis客户端

Redis自带一个客户端 `redis-cli` ，他提供的参数如下

* `-h <hostname>` 连接的地址

* `-p <port>` 连接端口

* `-a <password>` 设置登陆口令，如果配置了 `requirepass` 才需要提供，或者连接后使用 `AUTH` 命令认证

* `-n <Database ID>` 连接后选择的数据库

```shell
$ redis-cli 
127.0.0.1:6379> exit
$ 
```



## Redis配置（基于redis-cli）

使用redis-cli配置就是改变Redis的redis.conf文件，也就是说可以直接去修改配置文件，和使用redis-cli连接redis后修改的效果一样。在redis-cli中使用 `CONFIG` 命令查看和修改配置。



### 获取配置和更改配置

查看配置的语法：

```
127.0.0.1:6379> CONFIG GET <配置项>
```

配置项就是配置参数的名字，也可以使用通配符 `*` 表示匹配所有：

```
127.0.0.1:6379> CONFIG GET *
  1) "dbfilename"
  2) "dump.rdb"
  3) "requirepass"
  4) ""
  5) "masterauth"
  6) ""
  7) "cluster-announce-ip"
  8) ""
  9) "unixsocket"
 10) ""
 11) "logfile"
 12) ""
 13) "pidfile"
 14) ""
 15) "slave-announce-ip"
 16) ""
 17) "replica-announce-ip"
 18) ""
 19) "maxmemory"
 20) "0"
 21) "proto-max-bulk-len"
 22) "536870912"
 23) "client-query-buffer-limit"
 24) "1073741824"
 25) "maxmemory-samples"
 26) "5"
 27) "lfu-log-factor"
 28) "10"
 29) "lfu-decay-time"
 30) "1"
 31) "timeout"
 32) "0"
 33) "active-defrag-threshold-lower"
 34) "10"
 35) "active-defrag-threshold-upper"
 36) "100"
 37) "active-defrag-ignore-bytes"
 38) "104857600"
 39) "active-defrag-cycle-min"
 40) "5"
 41) "active-defrag-cycle-max"
 42) "75"
 43) "active-defrag-max-scan-fields"
 44) "1000"
 45) "auto-aof-rewrite-percentage"
 46) "100"
 47) "auto-aof-rewrite-min-size"
 48) "67108864"
 49) "hash-max-ziplist-entries"
 50) "512"
 51) "hash-max-ziplist-value"
 52) "64"
 53) "stream-node-max-bytes"
 54) "4096"
 55) "stream-node-max-entries"
 56) "100"
 57) "list-max-ziplist-size"
 58) "-2"
 59) "list-compress-depth"
 60) "0"
 61) "set-max-intset-entries"
 62) "512"
 63) "zset-max-ziplist-entries"
 64) "128"
 65) "zset-max-ziplist-value"
 66) "64"
 67) "hll-sparse-max-bytes"
 68) "3000"
 69) "lua-time-limit"
 70) "5000"
 71) "slowlog-log-slower-than"
 72) "10000"
 73) "latency-monitor-threshold"
 74) "0"
 75) "slowlog-max-len"
 76) "128"
 77) "port"
 78) "6379"
 79) "cluster-announce-port"
 80) "0"
 81) "cluster-announce-bus-port"
 82) "0"
 83) "tcp-backlog"
 84) "511"
 85) "databases"
 86) "16"
 87) "repl-ping-slave-period"
 88) "10"
 89) "repl-ping-replica-period"
 90) "10"
 91) "repl-timeout"
 92) "60"
 93) "repl-backlog-size"
 94) "1048576"
 95) "repl-backlog-ttl"
 96) "3600"
 97) "maxclients"
 98) "10000"
 99) "watchdog-period"
100) "0"
101) "slave-priority"
102) "100"
103) "replica-priority"
104) "100"
105) "slave-announce-port"
106) "0"
107) "replica-announce-port"
108) "0"
109) "min-slaves-to-write"
110) "0"
111) "min-replicas-to-write"
112) "0"
113) "min-slaves-max-lag"
114) "10"
115) "min-replicas-max-lag"
116) "10"
117) "hz"
118) "10"
119) "cluster-node-timeout"
120) "15000"
121) "cluster-migration-barrier"
122) "1"
123) "cluster-slave-validity-factor"
124) "10"
125) "cluster-replica-validity-factor"
126) "10"
127) "repl-diskless-sync-delay"
128) "5"
129) "tcp-keepalive"
130) "300"
131) "cluster-require-full-coverage"
132) "yes"
133) "cluster-slave-no-failover"
134) "no"
135) "cluster-replica-no-failover"
136) "no"
137) "no-appendfsync-on-rewrite"
138) "no"
139) "slave-serve-stale-data"
140) "yes"
141) "replica-serve-stale-data"
142) "yes"
143) "slave-read-only"
144) "yes"
145) "replica-read-only"
146) "yes"
147) "slave-ignore-maxmemory"
148) "yes"
149) "replica-ignore-maxmemory"
150) "yes"
151) "stop-writes-on-bgsave-error"
152) "yes"
153) "daemonize"
154) "no"
155) "rdbcompression"
156) "yes"
157) "rdbchecksum"
158) "yes"
159) "activerehashing"
160) "yes"
161) "activedefrag"
162) "no"
163) "protected-mode"
164) "no"
165) "repl-disable-tcp-nodelay"
166) "no"
167) "repl-diskless-sync"
168) "no"
169) "aof-rewrite-incremental-fsync"
170) "yes"
171) "rdb-save-incremental-fsync"
172) "yes"
173) "aof-load-truncated"
174) "yes"
175) "aof-use-rdb-preamble"
176) "yes"
177) "lazyfree-lazy-eviction"
178) "no"
179) "lazyfree-lazy-expire"
180) "no"
181) "lazyfree-lazy-server-del"
182) "no"
183) "slave-lazy-flush"
184) "no"
185) "replica-lazy-flush"
186) "no"
187) "dynamic-hz"
188) "yes"
189) "maxmemory-policy"
190) "noeviction"
191) "loglevel"
192) "notice"
193) "supervised"
194) "no"
195) "appendfsync"
196) "everysec"
197) "syslog-facility"
198) "local0"
199) "appendonly"
200) "no"
201) "dir"
202) "/data"
203) "save"
204) "3600 1 300 100 60 10000"
205) "client-output-buffer-limit"
206) "normal 0 0 0 slave 268435456 67108864 60 pubsub 33554432 8388608 60"
207) "unixsocketperm"
208) "0"
209) "slaveof"
210) ""
211) "notify-keyspace-events"
212) ""
213) "bind"
214) ""
```

这里**奇数行号是配置项的名称**，**偶数行号的是配置项的值**。



修改配置项的命令如下：

```
127.0.0.1:6379> CONFIG SET <配置项> <配置值>
```

例如：

```
127.0.0.1:6379> CONFIG SET loglevel "notice"
OK
```



### 一些重要配置

* `daemonize` ：Redis是否以守护进程方式运行，默认为 `no` ，如果要以守护进程方式运行改成 `yes` 
* `pidfile`：如果Redis以守护进程方式运行则把pid写到这个配置指定的pid文件中
* `port`：Redis监听的端口，默认6379
* `bind`：绑定的主机地址
* `timeout`：客户端闲置多久后关闭连接
* `loglevel`：日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning
* `logfile`：日志记录的位置
* `databases`：数据库的数量，默认有16个，默认处于0号数据库，可以使用`SELECT <DB ID>` 切换数据库
* `save`：指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合，格式为 `经过的秒数 修改的次数`。默认是`3600 1 300 100 60 10000`，也就是3600秒内一次更改，300秒内100次更改，60秒内10000次更改
* `rdbcompression`：指定存储至本地数据库时是否压缩数据，默认为 `yes`，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大
* `dbfilename`：本地数据库文件名，默认是dump.rdb
* `dir`：本地数据库保存的目录
* `slaveof`：设置当本机为slave服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步，他的格式为 `masterIP地址 master端口`
* `masterauth`：当master服务设置了密码保护时，slave服务连接master的密码
* `requirepass`：Redis密码，默认没有设置，如果设置了在连接Redis后腰使用`AUTH <password>` 认证
* `maxclients`：同一时间内的最大连接数
* `maxmemory`：Redis的最大内存限制，单位是bytes
* `appendonly`：指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失
* `appendfilename`：指定更新日志文件名，默认是appendonly.aof
* `appendfsync`：更新日志条件，有三个值可以指定：`no` 表示等操作系统进行数据缓存同步到磁盘（快），` always` 表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全），`everysec` 表示每秒同步一次（折衷，默认值）



## 备份数据

备份数据只需要执行 `SAVE` 命令即可，他会在 `dir` 目录下生成一个 `dbfilename` 的文件，在下次重启是会自动读取这个文件

```
127.0.0.1:6379> SAVE
OK
```

也可以使用后台执行版本

```
127.0.0.1:6379> BGSAVE
Background saving started
```



## 配置登陆口令

只需要配置 `requirepass` 就会开启登陆口令

```
127.0.0.1:6379> CONFIG SET requirepass 123456
OK
```

然后退出重新连接，执行命令时需要认证

```
127.0.0.1:6379> PING
(error) NOAUTH Authentication required.
```

使用 `AUTH` 命令做认证

```
127.0.0.1:6379> AUTH 123456
OK
127.0.0.1:6379> PING
PONG
```



## 性能测试

Redis自带性能测试的命令 `redis-benchmark`

命令参数：

* `-h` 指定服务器主机名
* `-p` 指定服务器端口
* `-s` 指定服务器 socket
* `-c` 指定并发连接数
* `-n` 指定请求数
* `-d` 以字节的形式指定 SET/GET 值的数据大小
* `-l` 生成循环，永久执行测试

```shell
$ redis-benchmark 
====== PING_INLINE ======
  100000 requests completed in 2.71 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

66.21% <= 1 milliseconds
99.90% <= 2 milliseconds
99.95% <= 3 milliseconds
99.99% <= 4 milliseconds
100.00% <= 4 milliseconds
36832.41 requests per second

====== PING_BULK ======
  100000 requests completed in 2.71 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

67.09% <= 1 milliseconds
99.95% <= 2 milliseconds
100.00% <= 2 milliseconds
36941.26 requests per second

====== SET ======
  100000 requests completed in 2.71 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

67.04% <= 1 milliseconds
99.88% <= 2 milliseconds
100.00% <= 2 milliseconds
36913.99 requests per second

====== GET ======
  100000 requests completed in 2.67 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

68.50% <= 1 milliseconds
99.97% <= 2 milliseconds
100.00% <= 2 milliseconds
37425.15 requests per second

====== INCR ======
  100000 requests completed in 2.67 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

68.84% <= 1 milliseconds
99.95% <= 2 milliseconds
100.00% <= 2 milliseconds
37453.18 requests per second

====== LPUSH ======
  100000 requests completed in 2.72 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

66.55% <= 1 milliseconds
99.78% <= 2 milliseconds
99.95% <= 3 milliseconds
99.97% <= 4 milliseconds
100.00% <= 4 milliseconds
36710.72 requests per second

====== RPUSH ======
  100000 requests completed in 2.67 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

68.82% <= 1 milliseconds
99.91% <= 2 milliseconds
99.97% <= 3 milliseconds
100.00% <= 3 milliseconds
37425.15 requests per second

====== LPOP ======
  100000 requests completed in 2.68 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

68.35% <= 1 milliseconds
99.84% <= 2 milliseconds
100.00% <= 2 milliseconds
37341.30 requests per second

====== RPOP ======
  100000 requests completed in 2.65 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

69.67% <= 1 milliseconds
99.91% <= 2 milliseconds
99.99% <= 3 milliseconds
100.00% <= 3 milliseconds
37678.97 requests per second

====== SADD ======
  100000 requests completed in 2.58 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

72.65% <= 1 milliseconds
99.79% <= 2 milliseconds
99.97% <= 3 milliseconds
100.00% <= 4 milliseconds
100.00% <= 4 milliseconds
38744.67 requests per second

====== HSET ======
  100000 requests completed in 2.62 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

70.67% <= 1 milliseconds
99.93% <= 2 milliseconds
99.96% <= 3 milliseconds
100.00% <= 4 milliseconds
38240.92 requests per second

====== SPOP ======
  100000 requests completed in 2.66 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

69.08% <= 1 milliseconds
99.95% <= 2 milliseconds
99.95% <= 3 milliseconds
99.99% <= 4 milliseconds
100.00% <= 4 milliseconds
37636.43 requests per second

====== LPUSH (needed to benchmark LRANGE) ======
  100000 requests completed in 2.65 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

69.37% <= 1 milliseconds
99.96% <= 2 milliseconds
100.00% <= 2 milliseconds
37707.39 requests per second

====== LRANGE_100 (first 100 elements) ======
  100000 requests completed in 2.70 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

67.20% <= 1 milliseconds
99.96% <= 2 milliseconds
100.00% <= 2 milliseconds
37064.49 requests per second

====== LRANGE_300 (first 300 elements) ======
  100000 requests completed in 2.70 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

67.66% <= 1 milliseconds
99.96% <= 2 milliseconds
100.00% <= 2 milliseconds
37078.23 requests per second

====== LRANGE_500 (first 450 elements) ======
  100000 requests completed in 2.65 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

69.76% <= 1 milliseconds
99.80% <= 2 milliseconds
99.99% <= 3 milliseconds
100.00% <= 3 milliseconds
37693.18 requests per second

====== LRANGE_600 (first 600 elements) ======
  100000 requests completed in 2.67 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

68.90% <= 1 milliseconds
99.91% <= 2 milliseconds
99.97% <= 3 milliseconds
100.00% <= 3 milliseconds
37439.16 requests per second

====== MSET (10 keys) ======
  100000 requests completed in 2.90 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

58.69% <= 1 milliseconds
99.78% <= 2 milliseconds
99.95% <= 3 milliseconds
99.96% <= 4 milliseconds
100.00% <= 5 milliseconds
34506.55 requests per second
```

