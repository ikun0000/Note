### SpringBoot使用yml注入实体类

Entity:

```java
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dog {
    private String name;
    private int age;
}

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ConfigurationProperties("person")
public class Person {
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birthday;
    private Map<String, String> maps;
    private List<String> list;
    private Dog dog;
    @Value("${pp.blog}")
    private String blog;
}
```

properties:

```yaml
person:
  name: aa
  age: 20
  happy: false
  birthday: 5/23/2001			# 日期格式：month/day/year
  maps: {game: gta, eat: burger}
  list: [aa, bb, cc]
  dog:
    name: ${person.name}_dog    # 支持变量引用，支持EL表达式
    age: ${person.age:10}

pp.blog: http://example.com
```



### 读取配置文件的优先级和位置

1. `file:./config/`
2. `file:./`
3. `classpath:/config/`
4. `classpath:/`



### SpringBoot多环境切换

#### 1. 多文件配置

配置文件位置

```
resources
	+ application.yml
	+ application-dev.yml
	+ application-prod.yml
```

application-dev.yml

```yaml
server:
  port: 8080
```

application-prod.yml

```yaml
server:
  port: 80
```

application.yml

```yaml
spring:
  profiles:
    active: dev
```



#### 2.单文件多模块配置

目录结构

```
resources
	+ application.yml
```

application.yml

```yaml
---
spring:
  profiles:
    active: dev
---
server:
  port: 8081
spring:
  profiles: dev
---
server:
  port: 80
spring:
  profiles: prod
---
```

