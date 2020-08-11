# Redis数据类型及其操作

Redis提供了8种数据类型，包括字符串（String）、散列（Hash）、列表（List）、集合（Set）、有序集合（Zset）、位图（bitmaps）、hyperloglogs和地理空间（geospatial）。其中使用的最多的前5中，而在前5种中使用最频繁的是字符串类型



## 字符串（String）

字符串类型很好理解，一个key对应的value是字符串，就好比一个redis是java中的HashMap，那么key的类型是String，value的类型也是String，在Redis中，字符串是安全的，可以存任何东西。

设置/获取字符串的值，设置key1的值为value1，然后获取key1的值：

```
127.0.0.1:6379> SET key1 value1
OK
127.0.0.1:6379> GET key1
"value1"
```

获取/设置字符串的子字符串，获取key1从下标2到下标5的值（包括下标5），设置key1从下标3开始的值为new：

```
127.0.0.1:6379> GETRANGE key1 2 5
"lue1"
127.0.0.1:6379> SETRANGE key1 3 new
(integer) 6
127.0.0.1:6379> GET key1
"valnew"
```

获取一个key的旧值同时赋新值，获取key1的旧值并赋新址value2：

```
127.0.0.1:6379> GETSET key1 value2
"value1"
```

一次设置/获取多个k-v键值对，设置key2为value2，key3为value3，然后同时获取key2和key3的值：

```
127.0.0.1:6379> MSET key2 value2 key3 value3
OK
127.0.0.1:6379> MGET key2 key3
1) "value2"
2) "value3"
```

创建一个键值对并设置过期时间，设置key4的值为value4并设置5秒后过期，设置key7为value7，并设置过期时间为6000毫秒：

```
127.0.0.1:6379> SETEX key4 5 value4
OK
127.0.0.1:6379> PSETEX key7 6000 value7
OK
```

在key不存在时创建键值对，key1存在返回0并且不再设置值，key5不存在返回1并设置值

```
127.0.0.1:6379> SETNX key1 value1
(integer) 0
127.0.0.1:6379> SETNX key5 value5
(integer) 1
```

在多个key不存在的时候设置值，如果有一个key设置失败那么所有就会设置失败，设置key5为value5，key6为value6，但是key5已经存在，所以设置失败，

```
127.0.0.1:6379> MSETNX key5 value5 key6 value6
(integer) 0
127.0.0.1:6379> MSETNX key6 value6 key7 value7
(integer) 1
```

获取字符串的长度，获取key1的长度：

```
127.0.0.1:6379> STRLEN key1
(integer) 6
```

如果value存的值是数字字符串，则可以进行自增自减，设置key1为1，并自增两次然后自减一次：

```
127.0.0.1:6379> SET key1 1
OK
127.0.0.1:6379> INCR key1
(integer) 2
127.0.0.1:6379> INCR key1
(integer) 3
127.0.0.1:6379> DECR key1
(integer) 2
```

如果value是数字字符串，则可以进行加减法运算，设置key2为10，然后加10再减3：

```
127.0.0.1:6379> SET key2 1
OK
127.0.0.1:6379> INCRBY key2 10
(integer) 11
127.0.0.1:6379> DECRBY key2 3
(integer) 8
```

连接字符串，给key1设置foo然后再拼上bar

```
127.0.0.1:6379> SET key1 foo
OK
127.0.0.1:6379> APPEND key1 bar
(integer) 6
127.0.0.1:6379> GET key1
"foobar"
```

截取字符串，和 `GETRANGE` 类似：

```
127.0.0.1:6379> SET key1 abcdefghijk
OK
127.0.0.1:6379> SUBSTR key1 2 7
"cdefgh"
```



## 列表（List）

Redis支持value是一个列表，列表是一个有序可重复的数据结构，相当于java里的ArrayList，在java中相当于在HashMap中的value是ArrayList。

从列表的左端插入值，从list1的左边插入v1、v2、v3、v4：

```
127.0.0.1:6379> LPUSH list1 v1 v2 v3 v4
(integer) 4
```

从指定下表开始遍历列表到指定下标结束，遍历所有那么第二个下标写-1：

```
127.0.0.1:6379> LRANGE list1 0 -1
1) "v4"
2) "v3"
3) "v2"
4) "v1"
```

获取列表的元素个数：

```
127.0.0.1:6379> LLEN list1
(integer) 4
```

获取指定下标的元素：

```
127.0.0.1:6379> LINDEX list1 2
"v2"
```

在列表的指定位置插入值，在list1的v3前面插入i1，之后插入i2：

```
127.0.0.1:6379> LINSERT list1 BEFORE v3 i1
(integer) 5
127.0.0.1:6379> LINSERT list1 AFTER v3 i2
(integer) 6
127.0.0.1:6379> LRANGE list1 0 -1
1) "v4"
2) "i1"
3) "v3"
4) "i2"
5) "v2"
6) "v1"
```

从列表的右边插入元素：

```
127.0.0.1:6379> RPUSH list1 r1 r2
(integer) 8
127.0.0.1:6379> LRANGE list1 0 -1
1) "v4"
2) "i1"
3) "v3"
4) "i2"
5) "v2"
6) "v1"
7) "r1"
8) "r2"
```

修改列表指定下标的值，修改list1下标为2的值为new：

```
127.0.0.1:6379> LSET list1 2 new
OK
127.0.0.1:6379> LINDEX list1 2
"new"
```

截取列表指定位置的元素到原列表，截取list1从下标2到下标6（包括6）的元素并赋值为list1：

```
127.0.0.1:6379> LTRIM list1 2 6
OK
127.0.0.1:6379> LRANGE list1 0 -1
1) "new"
2) "i2"
3) "v2"
4) "v1"
5) "r1"
```

删除列表中指定个数的元素，删除list1中1个i2元素：

```
127.0.0.1:6379> LREM list1 1 i2
(integer) 1
```

从列表的左边/右边弹出一个元素：

```
127.0.0.1:6379> LPOP list1
"new"
127.0.0.1:6379> RPOP list1
"r1"
```



## 集合（Set）

Redis中的集合是无序且元素不能重复的，类似于java中的HashSet。

向集合内添加元素，集合不存在就会创建，并返回添加成功元素个数：

```
127.0.0.1:6379> SADD set1 1 2 3 4 5 5
(integer) 5
```

获取集合中元素的个数：

```
127.0.0.1:6379> SCARD set1
(integer) 5
```

遍历集合的所有元素：

```
127.0.0.1:6379> SMEMBERS set1
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
```

对集合拍寻并返回，对set1分别升序、降序排序：

```
127.0.0.1:6379> SORT set1 ASC
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
127.0.0.1:6379> SORT set1 DESC
1) "5"
2) "4"
3) "3"
4) "2"
5) "1"
```

判断元素是否在集合中，是返回1，否则返回0，分别判断1和7是否在set1中：

```
127.0.0.1:6379> SISMEMBER set1 1
(integer) 1
127.0.0.1:6379> SISMEMBER set1 7
(integer) 0
```

从集合中随机返回n个元素，但不删除集合内的元素，从set1中随机返回3个元素：

```
127.0.0.1:6379> SRANDMEMBER set1 3
1) "5"
2) "2"
3) "3"
```

从集合中删除指定元素，删除成功返回1，否则返回i0，从set1中删除3：

```
127.0.0.1:6379> SREM set1 3
(integer) 1
127.0.0.1:6379> SREM set1 3
(integer) 0
```

从集合中弹出n个元素，从set1中弹出2个元素：

```
127.0.0.1:6379> SPOP set1 2
1) "4"
2) "5"
```

获取第一个集合中不属于第二个集合的元素，获取set1中不属于set2中的元素：

```
127.0.0.1:6379> SADD set1 1 2 3 4 5
(integer) 5
127.0.0.1:6379> SADD set2 4 5 6 7 8
(integer) 5
127.0.0.1:6379> SDIFF set1 set2
1) "1"
2) "2"
3) "3"
```

获取集合的交集，获取set1和set2的交集：

```
127.0.0.1:6379> SINTER set1 set2
1) "4"
2) "5"
```

获取集合的并集，获取set1和set2的并集：

```
127.0.0.1:6379> SUNION set1 set2
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
6) "6"
7) "7"
8) "8"
```

获取集合1中不属于集合2中的元素并添加到新的集合中，向set3中添加set1中不属于set2的元素：

```
127.0.0.1:6379> SDIFFSTORE set3 set1 set2
(integer) 3
127.0.0.1:6379> SMEMBERS set3
1) "1"
2) "2"
3) "3"
```

移动一个集合的元素到另一个集合中，把set1中的1移动到set2中：

```
127.0.0.1:6379> SMOVE set1 set2 1
(integer) 1
```

获取集合的并集并添加到新的集合中，获取set1和set2的并集并添加到set4中：

```
127.0.0.1:6379> SUNIONSTORE set4 set1 set2
(integer) 8
```

获取集合的交集并添加到新集合中，获取set1和set2的交集并添加到set5中：

```
127.0.0.1:6379> SINTERSTORE set5 set1 set2
(integer) 2
```



## 哈希（Hash）

哈希就是一个键值对，也就是在Redis的键值对数据库上再添加一个键值对，就像java中HashMap中的value再嵌套一个HashMap。

一次/多次设置哈希中的值，向hash1中添加f1-v1、f2-v2和f3-v3：

```
127.0.0.1:6379> HSET hash1 f1 v1
(integer) 1
127.0.0.1:6379> HMSET hash1 f2 v2 f3 v3
OK
```

从哈希中一次/多次获取值，在hash1中获取f1、f2和f3对应的值：

```
127.0.0.1:6379> HGET hash1 f1
"v1"
127.0.0.1:6379> HMGET hash1 f2 f3
1) "v2"
2) "v3"
```

获取哈希中所有的键值对，奇数行是key，偶数行是value，获取hash1中所有的键值对：

```
127.0.0.1:6379> HGETALL hash1
1) "f1"
2) "v1"
3) "f2"
4) "v2"
5) "f3"
6) "v3"
```

获取哈希中有多少对键值对，获取hash1中的键值对个数：

```
127.0.0.1:6379> HLEN hash1
(integer) 3
```

获取哈希中的所有key/value，分别获取hash1中所有的key和value：

```
127.0.0.1:6379> HKEYS hash1
1) "f1"
2) "f2"
3) "f3"
127.0.0.1:6379> HVALS hash1
1) "v1"
2) "v2"
3) "v3"
```

获取哈希中某个key是否被定义了，是返回1，否则返回0，获取hash1中f3和f5是否被定义：

```
127.0.0.1:6379> HEXISTS hash1 f3
(integer) 1
127.0.0.1:6379> HEXISTS hash1 f5
(integer) 0
```

删除哈希中一个键值对，删除hash1中的f1：

```
127.0.0.1:6379> HDEL hash1 f1
(integer) 1
```

在哈希中创建键值对，但是只有当key没被定义时创建，在hash1中分别创建f1-v1，f2-v2，成功返回1，否则返回0：

```
127.0.0.1:6379> HSETNX hash1 f1 v1
(integer) 1
127.0.0.1:6379> HSETNX hash1 f2 v2
(integer) 0
```

获取哈希中指定key的value的长度，获取hash1中f1对应value的长度：

```
127.0.0.1:6379> HSTRLEN hash1 f1
(integer) 2
```

如果哈希中的value是数字字符串，那么可以对他直接做加法运算，对hash1中f5的value加5：

```
127.0.0.1:6379> HSET hash1 f5 1
(integer) 1
127.0.0.1:6379> HINCRBY hash1 f5 5
(integer) 6
```



## 有序集合（Zsort）

有序集合和集合一样也是string类型的集合且不能有重复元素。不同于集合的是，有序集合中每个元素都会关联一个double类型的分数，Redis会根据分数对有序集合从小到大排序，虽然元素不能有重复，但是分数是可以重复的。

向有序集合中添加元素，想zset1中添加三个元素，v1的分数是1.0，v2的分数是2.0，v3的分数是2.1：

```
127.0.0.1:6379> ZADD zset 1.0 v1 2.0 v2 2.1 v3
(integer) 3
```

获取有序集合中的元素个数，获取zset1中的元素个数：

```
127.0.0.1:6379> ZCARD zset1
(integer) 3
```

遍历有序集合内指定下标内的所有元素（分数从小到大和从大到小），如果遍历全部则第二个为-1：

```
127.0.0.1:6379> ZRANGE zset1 0 -1
1) "v1"
2) "v2"
3) "v3"
127.0.0.1:6379> ZREVRANGE zset11 0 -1
1) "v3"
2) "v2"
3) "v1"
```

计算有序集合中元素分数在指定区间中的元素个数：

```
127.0.0.1:6379> ZCOUNT zset1 0 2
(integer) 2
```

计算指定元素在有序集合内的等级（从小到大和从大到校），计算v2和v3在zset1中从小到大的排名，计算v3hev1在zset11中从大到小的排名：

```
127.0.0.1:6379> ZRANK zset1 v2
(integer) 1
127.0.0.1:6379> ZRANK zset1 v3
(integer) 2
127.0.0.1:6379> ZREVRANK zset11 v3
(integer) 0
127.0.0.1:6379> ZREVRANK zset11 v1
(integer) 2
```

对有序集合中的元素在指定分数区间内进行分数排序（从小到大），对zset1的分数为0到3的元素排序：

```
127.0.0.1:6379> ZRANGEBYSCORE zset1 0 3
1) "v1"
2) "v2"
3) "v3"
```

对有序集合内的指定元素的分数做加法，对zset1中的v2的分数加3：

```
127.0.0.1:6379> ZINCRBY zset1 3 v2
"5"
```

从有序集合中弹出分数最大/最小的元素，从zset1中弹出分数最大和最小元素：

```
127.0.0.1:6379> ZPOPMAX zset1 1
1) "v2"
2) "5"
127.0.0.1:6379> ZPOPMIN zset1 1
1) "v1"
2) "1"
```

删除有序集合中的指定元素，删除zset1中v3元素：

```
127.0.0.1:6379> ZREM zset1 v3
(integer) 1
```

获取有序集合中指定元素的分数，获取zset11中v2的分数：

```
127.0.0.1:6379> ZSCORE zset11 v2
"2"
```



## 位图（bitmaps）

位图不是实际的数据类型，他只是string类型上一组面向位的操作，他的操作方式设置某个偏移量的位的二进制值。

设置一个key在某个偏移量的值，设置bit1偏移0位和偏移2为的值为1，偏移1位为0：

```
127.0.0.1:6379> SETBIT bit1 0 1
(integer) 0
127.0.0.1:6379> SETBIT bit1 1 0
(integer) 0
127.0.0.1:6379> SETBIT bit1 2 1
(integer) 0
```

获取指定key的偏移量的值：

```
127.0.0.1:6379> GETBIT bit1 0
(integer) 1
127.0.0.1:6379> GETBIT bit1 2
(integer) 1
127.0.0.1:6379> GETBIT bit1 1
(integer) 0
```



## HyperLogLog

Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定 的、并且是很小的。

### 基数

比如数据集 {1, 3, 5, 7, 5, 7, 8}， 那么这个数据集的基数集为 {1, 3, 5 ,7, 8}, 基数(不重复元素)为5。 基数估计就是在误差可接受的范围内，快速计算基数。



添加指定元素到HyperLogLog中，向hl中添加元素：

```
127.0.0.1:6379> PFADD hl 1 3 5 5 7 11
(integer) 1
```

获取HyperLogLog的基数估算值：

```
127.0.0.1:6379> PFCOUNT hl
(integer) 5
```

合并两个HyperLogLog，把hl和hll合并到hl中：

```
127.0.0.1:6379> PFMERGE hl hll
OK
```



## 地理空间（geospatial）

Redis允许使用经纬度存储一个地理位置，并且可以对添加的地理位置做一些操作。



向geospatial中添加地理位置，向Sicily中添加经纬度为13.361389、38.115556的Palermo和经纬度为15.087269、37.502669的Catania

```
127.0.0.1:6379> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2
```

返回处于给定经纬度和半径内的地理位置，方圆半径单位：`km`、`m`、`mi`（英里）、`ft`（英尺），返回Sicily在经纬度15和37内方圆100km内的位置：

```
127.0.0.1:6379> GEORADIUS Sicily 15 37 100 km
1) "Catania"
```

