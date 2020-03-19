### SpringBoot使用缓存

SpringBoot使用缓存首先要在配置类或者住启动类上添加一个开启缓存的注解`@EnableCaching`

```java
@SpringBootApplication
@EnableCaching
public class LearndemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(LearndemoApplication.class, args);
	}
}
```



缓存一个方法

```java
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @Cacheable(cacheNames = {"emp"},key = "#id", condition = "#id>0")
    @GetMapping("/emp/{id}")
    public Employee getEmpById(@PathVariable("id") int id) {
        System.out.println("query");
        return employeeDao.getEmpById(id);
    }

}
```

`@Cacheable`会对标注的方法进行缓存。

* `cacheNames`参数指定缓存的名字，可以一次存储在多个缓存中。
* `key`制定缓存的key，默认使用参数作为缓存的key，支持spEL表达式，比如上面的例子使用`#id`，`#a0`，`#p0`，`#root.args[0]`都是表示获取第一个参数，
* `#result`表示返回值，`#root.method.name`取得方法名，`#root.target`获取当前被调用的对象，`#root.targetClass`代表被调用的目标对象的类。
* `condition`用来指定什么情况下才对函数进行缓存，同样支持spEL表达式。
* `keyGenerator`指定使用一个`KeyGenerator`的bean名称。



定义`KeyGenerator`

```java
@Configuration
public class MyConfig {

    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return method.getName();
            }
        };
    }

}
```

> `KeyGenerator`是`org.springframework.cache.interceptor.KeyGenerator`的



更新缓存

```java
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @CachePut(cacheNames = {"emp"}, key = "#employee.getId()")
    @PostMapping("/emp")
    public Employee updateEmp(Employee employee) {
        employeeDao.updateEmp(employee);
        return employee;
    }

}
```

`@CachePut`会先调用方法，等调用完后再对结果进行缓存（更新缓存），注解参数和`@Cacheable`一致



清除缓存

```java
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @CacheEvict(cacheNames = {"emp"}, key = "#id")
    @DeleteMapping("/emp/{id}")
    public Employee deleteEmp(@PathVariable("id") int id) {
        Employee employee = employeeDao.getEmpById(id);
        employeeDao.deleteEmp(id);
        return employee;
    }

}
```

`@CacheEvict`会对在调用方法时候删除`cacheNames`的`key`的缓存，设置`allEntries`为true清空缓存的所有数据，设置`beforeInvocation`为true会在方法执行前删除缓存



> `@Caching`是`Cacheable`，`@CachePut`，`@CacheEvict`的集合体，可以一次设置多个缓存策略
> 
> ```java
>     @Caching(cacheable = {
>             @Cacheable(value = "emp", key = "#lastName")
>     },
>     put = {
>             @CachePut(value = "emp", key = "#result.id"),
>             @CachePut(value = "emp", key = "#result.email")
>     })
> ```
>
> 



如果一个类里面的方法都缓存到同一个缓存中，那么可以使用`@CacheConfig`注解来指定一些公共的配置，例如`cacheNames`

```java
@CacheConfig(cacheNames = {"emp"})
@RestController
public class EmployeeController {
	// CRUD Operations
}
```



### SpringBoot整合Redis

docker安装redis

```shell
$ docker run --name some-redis -d -p 6379:6379 redis
```



导入redis依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

添加redis配置

application.yml

```yaml
spring:
  redis:
    port: 6379
    host: 10.10.10.246
    database: 0
    jedis:
      pool:
        max-active: 8
        max-wait: 2ms
        min-idle: 0
        max-idle: 8
```

操作redis使用下面两个类

```java
@Autowired
private RedisTemplate redisTemplate;

@Autowired
private StringRedisTemplate stringRedisTemplate;
```

`StringRedisTemplate`是为了简化操作字符串的

`RedisTemplate`的`redisTemplate.bound*Ops(String key)`返回一个`Bound*Operations`用来绑定key对应的对象，之后使用返回的对象就可以操作对应key的对象了

例如绑定key为cache的List对象

```java
BoundListOperations cache = redisTemplate.boundListOps("cache");
cache.leftPush("c1");
cache.leftPush("c2");
```

`redisTemplate.opsFor*()`会返回i一个`*Operations`的对象用于操作该数据类型的对象，这个没有key的约束

例如绑定Hash

```java
HashOperations hashOperations = redisTemplate.opsForHash();
hashOperations.put("key1", "hashKey1", "obj1");
```

API和redis命令大致一样

> 存储对象的时候对象需要实现`Serializable`接口



修改`RedisTemplate`的序列化规则

使用JSON序列化规则

```java
redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
```



### 使用Redis做缓存

application.yml

```yaml
  cache:
    type: REDIS
    redis:
      cache-null-values: true		# 缓存空值
      key-prefix: testdemo			# 设置统一的前缀，默认是`函数名::`
      time-to-live: 10d				# 缓存的时间，默认是一直缓存
      use-key-prefix: true			# 是否在写入时带上前缀
```

> 注意被缓存的对象一定要实现`Serializable`接口，否则在缓存的时候会抛出`java.io.InvalidClassException`异常