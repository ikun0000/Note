# RocketMQ安装和坑

[RocketMQ基本概念]( https://github.com/apache/rocketmq/blob/master/docs/cn/concept.md )



## 安装前需要的配置

* 64位的操作系统
* 64位 JDK 1.8 +
* Maven 3.2.x +
* Git
* 磁盘有至少4g的空间分配给 Broker Server



## 开始安装和坑

1. 去[官网下载RocketMQ]( http://rocketmq.apache.org/dowloading/releases/ )

2. 如果下载了Source版本的话需要自己编译

   ```shell
   > unzip rocketmq-all-4.7.1-source-release.zip
   > cd rocketmq-all-4.7.1/
   > mvn -Prelease-all -DskipTests clean install -U
   > cd distribution/target/rocketmq-4.7.1/rocketmq-4.7.1
   ```

3. 然后进入RocketMQ的家目录，开启Name Server

   ```shell
   > nohup sh bin/mqnamesrv &
     [1] 8439
   [root@localhost rocketmq]# nohup: ignoring input and appending output to ‘nohup.out’
   
   [1]+  Exit 1                  nohup sh bin/mqnamesrv
   ```

   然后查看nohup.out

   ```shell
   > cat nohup.out 
   Invalid initial heap size: -Xms4g
   The specified size exceeds the maximum representable size.
   Error: Could not create the Java Virtual Machine.
   Error: A fatal exception has occurred. Program will exit.
   ```

   除非你的内存足够大，否则分配内存就会失败，这时要去修改**bin/runserver.sh**，并找到下面这行

   ```shell
   #===========================================================================================
   # JVM Configuration
   #===========================================================================================
   # The RAMDisk initializing size in MB on Darwin OS for gc-log
   
   ...
   
   JAVA_OPT="${JAVA_OPT} -server -Xms4g -Xmx4g -Xmn2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
   ```

   把**-Xms、-Xmx、-Xmn**的内存调小

   ```shell
   #===========================================================================================
   # JVM Configuration
   #===========================================================================================
   # The RAMDisk initializing size in MB on Darwin OS for gc-log
   
   ...
   
   JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
   ```

   然后再启动

   ```
   > nohup sh bin/mqnamesrv &
   > tail -f ~/logs/rocketmqlogs/namesrv.log
   The Name Server boot success...
   ```

4. 之后再启动Broker Server，当然如果内存太小也是启动不起来，原因和上面一样

   ```
   Invalid initial heap size: -Xms4g
   The specified size exceeds the maximum representable size.
   Error: Could not create the Java Virtual Machine.
   Error: A fatal exception has occurred. Program will exit.
   
   Invalid maximum direct memory size: -XX:MaxDirectMemorySize=15g
   The specified size exceeds the maximum representable size.
   Error: Could not create the Java Virtual Machine.
   Error: A fatal exception has occurred. Program will exit.
   ```

   另外Broker Server启动时还有另外一个参数需要调整，打开**bin/runbroker.sh**，找到这段

   ```shell
   #===========================================================================================
   # JVM Configuration
   #===========================================================================================
   # The RAMDisk initializing size in MB on Darwin OS for gc-log
   
   ...
   
   JAVA_OPT="${JAVA_OPT} -server -Xms4g -Xmx4g -Xmn2g"
   ...
   JAVA_OPT="${JAVA_OPT} -XX:MaxDirectMemorySize=15g"
   ```

   修改**-Xms、-Xmx、-Xmn和-XX:MaxDirectMemorySize**

   ```shell
   #===========================================================================================
   # JVM Configuration
   #===========================================================================================
   # The RAMDisk initializing size in MB on Darwin OS for gc-log
   
   ...
   
   JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn256m"
   ...
   JAVA_OPT="${JAVA_OPT} -XX:MaxDirectMemorySize=1g"
   ```

   然后启动broker

   ```shell
   > nohup sh bin/mqbroker -n localhost:9876 &
   > tail -f ~/logs/rocketmqlogs/broker.log 
   The broker[%s, 172.30.30.233:10911] boot success...
   ```

5. 测试安装成功，使用**bin/bools.sh**进行测试

   在测试之前需要告诉客户端（Producer和Consumer）名字服务器在哪里，最简单的方法就是设置 `NAMESRV_ADDR` 环境变量

   ```shell
   > export NAMESRV_ADDR=localhost:9876
   > sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
   SendResult [sendStatus=SEND_OK, msgId= ...
   
   > sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer
   ConsumeMessageThread_%d Receive New Messages: [MessageExt...
   ```

6. 关闭服务

   ```shell
   # 关闭broker
   > sh bin/mqshutdown broker
   The mqbroker(36695) is running...
   Send shutdown request to mqbroker(36695) OK
   
   # 关闭name server
   > sh bin/mqshutdown namesrv
   The mqnamesrv(36664) is running...
   Send shutdown request to mqnamesrv(36664) OK
   ```




# RocketMQ监控

[安装文档]( https://github.com/apache/rocketmq-externals/tree/master/rocketmq-console )

1. 下载[rocketmq-externals]( https://github.com/apache/rocketmq-externals )

   ```shell
   $ git clone https://github.com/apache/rocketmq-externals.git
   ```

2. 进入rocketmq-console

3. 编译rocketmq-console

   ```shell
   $ mvn clean package -Dmaven.test.skip=true docker:build
   ```

4. 启动控制台

   ```shell
   $ java -jar target/rocketmq-console-ng-2.0.0.jar
   ```



# 大坑



## org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException: sendDefaultImpl call timeout

如果使用java发送消息不成功而且有这个异常时：

### 重启Name Server

Name Server 的 IP 地址改成服务器的公网 IP 或者现在的局域网IP（只能在局域网内使用），不能写localhost和127.0.0.1：

```shell
$ nohup sh bin/mqnamesrv -n 10.10.10.246:9876 &
```



### 从其Broker

修改**${ROCKETMQ_HOME}/conf/broker.conf**，在配置项中添加 ` brokerIP1 ` 配置并设置为服务器的公网IP地址：

```
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
brokerIP1=10.10.10.246
```

启动broker，需要设置 `autoCreateTopicEnable=true` 让broker自动创建topic，否则需要自己手动创建：

```shell
$ nohup sh bin/mqbroker -n 10.10.10.246:9876 -c conf/broker.conf autoCreateTopicEnable=true &
```

