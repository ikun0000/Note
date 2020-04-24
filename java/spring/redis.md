# 基础命令

```shell
keys *						# 查看所有key
set key value				# 设置一个字符串键值对
exists key					# 查看key是否定义
move key db					# 移动key到别的数据库
get key						# 获取一个key
expire key time				# 给key定义过期时间
select db 					# 切换数据库
del key						# 删除key
type key					# 查看key的类型
ping						# 测试连接
flushdb						# 删除数据库所有的key-value
randomkey					# 随机获取key
save						# 保存
```

12

# String

```shell
append key value 			# 向key添加字符串
substr key arg arg			# 截取字符串
strlen key					# 获取字符串的长度
incr key					# key的值自增1，必须为数字
decr key					# key的值自减1，必须为数字
incrby key number			# key加上number，必须为数字
decrby key number			# key减去number，必须为数字
getrange key start end		# 获取范围的值
setrange key offset value	# 替换某个位置的值
setex key second value 		# 设置key-value并设置过期时间
setnx key value				# 设置key-value，如果key没有被定义
mset key value [key value]	# 批量设置值
mget key [key]				# 批量获取值
getset key value			# 设置key的新值并返回旧值
rename key newkey			# 改key名
flushdb 					# 刷新数据库
```

16

# List

```shell
lpush key value [value]		# 设置list并从左边插入数据
lrange key start end		# 获取list的从start到end的值
lpop key					# 从左边弹出一个值
lindex key index			# 从左边开始获取list指定下标
linsert key AFTER|BEFORE pivot value 	# 在list key的值得前面或者后面插入值
llen key					# 获取key的长度
ltrim key start end			# 只保留key list的start到end的值，并设置会key list
lset key index value		# 设置key list指定index的值
lrem key count value		# 移除key list的count个value的值
rpush key value [value]		# 在list从右边插入数据
rpop key					# 从list的右边弹出数据
rpoplpush src dest			# 从src的右边弹出一个value插到dest的左边
```

12

# Set

```shell
sadd key value [value]			# 添加数据到set
scard key						# 获取set的长度
sdiff key key 					# 获取差集
sdiffstore dest key [key]		# 比较dest和key的数据有什么不同并把key中的值放到dest
smembers key					# 获取key的所有成员
smove src dest member			# 把src中的member移动到dest中
sort key ASC|DESC				# 对key排序
spop key count					# 从key中弹出count个成员，会删除
srem key member [member]		# 删除member
sismember key member			# 判断member是否在key中
srandmember key count			# 从key中随机抽取两个元素，不删除
sinter key key					# 获取交集
sunion key key					# 获取并集
```

13

# ZSet

```shell
zadd key score member		# 设置zset的成员并设置值
zcard key					# 获取zset的数量
zcount key min max			# 获取zset中min到max的的数量
zincrby key incre member	# 给zset中的member加上incre
zpopmax key [count]			# 弹出zset中最大值的成员count个
zpopmin key [count]			# 弹出zset中最小值的成员count个
zrangebyscore key min max	# 对zset中的min到max中的member排序
zrank key member			# 获取zset中member从大到小的等级
zrem key member				# 删除一个zset的member
zrange key start end		# 查看zset从start到end的成员
```

10

# Hash

```shell
hset key field value				# 设置一个hash的field-value
hmset key field value [field value]	# 设置一个hash多可以field-value
hdel key field [field]				# 删除key-hash中的field
hget key field						# 获取hash中的field的值
hmget key field [field]				# 一次获取多个field
hgetall key							# 获取hash所有key-value
hexists key field					# 测试key-hash中的field是否定义了
hincrby key field increment			# 对key-hash中的field加incrment
hlen key							# 获取hash有多少个field-value
hkeys key							# 获取hash的所有key
hvals key							# 获取hash的所有value
hstrlen key field					# 获取hash中的field的长度
hsetnx key field value				# 当hash中的field不存在时创建field-value
```

13

# Bitmaps

```shell
> setbit key 10 1
(integer) 1
> getbit key 10
(integer) 1
> getbit key 11
(integer) 0

> setbit key 0 1
(integer) 0
> setbit key 100 1
(integer) 0
> bitcount key
(integer) 2
```



# Hyperloglog

```shell
  > pfadd hll a b c d
  (integer) 1
  > pfcount hll
  (integer) 4
```



# Geospatial

```shell
redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
(integer) 2
redis> GEODIST Sicily Palermo Catania
"166274.15156960039"
redis> GEORADIUS Sicily 15 37 100 km
1) "Catania"
redis> GEORADIUS Sicily 15 37 200 km
1) "Palermo"
2) "Catania"
redis> 
```



# 事务

```shell
discard					# 取消事务
exec					# 提交事务
multi					# 开始事务
unwatch					# 取消watch对key的监控
watch					# 监控key
```



# pub/sub

```shell
publish channel msg				# 发送消息到channel
subscribe channel				# 订阅channel
unsubscribe channel				# 取消订阅
psubscribe channel [channel]	# 订阅多个channel
punsubscribe channel [channel]	# 取消订阅多个
PUBSUB CHANNELS					# 查看订阅状态
```

