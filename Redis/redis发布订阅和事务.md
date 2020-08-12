# Redis发布订阅与事务



## 发布订阅

Redis 发布订阅(pub/sub)是一种消息通信模式：发送者(pub)发送消息，订阅者(sub)接收消息。也就是说Redis甚至可以完成部分消息队列的功能。

Redis客户端可以订阅任意数量的消息频道，频道则是用来保存发送来的消息并分发给订阅了该频道的处理程序。

订阅与发布消息，client1订阅了名为c1的频道：

```
127.0.0.1:6379> SUBSCRIBE c1
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "c1"
3) (integer) 1

```

给指定频道发送消息，client2给c1频道发送hello world消息：

```
127.0.0.1:6379> PUBLISH c1 "hello world!"
(integer) 1
```

client1收到消息：

```
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "c1"
3) (integer) 1
1) "message"
2) "c1"
3) "hello world!"
```

退订一个频道，退订c1频道：

```
127.0.0.1:6379> UNSUBSCRIBE c1
1) "unsubscribe"
2) "c1"
3) (integer) 0
```



订阅一个符合匹配模式的频道，匹配c开头的频道：

```
127.0.0.1:6379> PSUBSCRIBE c*
Reading messages... (press Ctrl-C to quit)
1) "psubscribe"
2) "c*"
3) (integer) 1

```

给c1、c2发送消息：

```
127.0.0.1:6379> PUBLISH c1 message1
(integer) 1
127.0.0.1:6379> PUBLISH c2 message2
(integer) 1
```

成功接收两条消息：

```
Reading messages... (press Ctrl-C to quit)
1) "psubscribe"
2) "c*"
3) (integer) 1
        1) "pmessage"
2) "c*"
3) "c1"
4) "message1"
1) "pmessage"
2) "c*"
3) "c2"
4) "message2"
```

退订指定模式的频道，退订所有c开头的频道：

```
127.0.0.1:6379> PUNSUBSCRIBE c*
1) "punsubscribe"
2) "c*"
3) (integer) 0
```



查看订阅与发布系统的状态，查看所有的活跃的频道：

```
127.0.0.1:6379> PUBSUB CHANNELS
1) "c2"
2) "c1"
```



## 事务

Redis 事务可以一次执行多个命令， 并且带有以下两个重要的保证：

* 事务是一个单独的隔离操作：事务中的所有命令都会序列化、按顺序地执行。事务在执行的过程中，不会被其他客户端发送来的命令请求所打断。
* 事务是一个原子操作：事务中的命令要么全部被执行，要么全部都不执行。

一个事务从开始到执行会经历以下三个阶段：

* 开始事务
* 命令入列
* 执行事务



监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断，监视key1和key2：

```
127.0.0.1:6379> WATCH key1 key2
OK
```

标记开始一个事务：

```
127.0.0.1:6379> MULTI
OK
```

取消事务：

```
127.0.0.1:6379> DISCARD
OK
```

执行事务内的所有命令：

```
127.0.0.1:6379> EXEC
```

取消 WATCH 命令对所有 key 的监视：

```
127.0.0.1:6379> UNWATCH
OK
```

张三给李四转账例子：

```
127.0.0.1:6379> MSET zhangsan 1000 lisi 800
OK
127.0.0.1:6379> WATCH zhangsan lisi
OK
127.0.0.1:6379> MULTI 
OK
127.0.0.1:6379> DECRBY zhangsan 100
QUEUED
127.0.0.1:6379> INCRBY lisi 100
QUEUED
127.0.0.1:6379> EXEC
1) (integer) 900
2) (integer) 900
127.0.0.1:6379> UNWATCH
OK
```

如果中间出了问题用 `DISCARD` 终止事务：

```
127.0.0.1:6379> MSET zhangsan 1000 lisi 800
OK
127.0.0.1:6379> WATCH zhangsan lisi
OK
127.0.0.1:6379> MULTI
OK
127.0.0.1:6379> DECRBY zhangsan 100
QUEUED
127.0.0.1:6379> DISCARD
OK
127.0.0.1:6379> UNWATCH 
OK
127.0.0.1:6379> MGET zhangsan lisi
1) "1000"
2) "800"
```

