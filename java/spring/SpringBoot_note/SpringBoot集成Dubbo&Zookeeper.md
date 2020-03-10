### SpringBoot集成Dubbo&Zookeeper

Dubbo架构

![](./img/e.png)

zookeeper下载配置

[下载地址]( https://zookeeper.apache.org/releases.html )

然后配置conf下的zoo.cfg（复制zoo_sample.cfg）为zoo.cfg

修改dataDir为自己本地的目录

然后再bin目录下zkServer运行zookeeper



安装dubbo-admin

[下载地址]( https://github.com/apache/dubbo-admin )，下载master分支

之后使用maven即可，在dubbo-admin下的target的jar包运行就可以了

编译问题

Maven编译显示`Caused by: org.apache.maven.plugin.compiler.CompilationFailureException: Compilation failure`

可能是编译时版本过低， 修改编译时的版本

```xml
<plugin>  
    <groupId>org.apache.maven.plugins</groupId>  
    <artifactId>maven-compiler-plugin</artifactId>  
    <configuration>  
        <source>1.8</source>  
        <target>1.8</target>  
    </configuration>  
</plugin>
```

访问http://127.0.0.1:7001

默认用户名：root

默认密码：root



在provider和consumer 导入Dubbo的SpringBoot Starter和Zookeeper Client

```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.5</version>
</dependency>

<dependency>
    <groupId>com.github.sgroschupf</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.1</version>
</dependency>

<!-- 引入zookeeper -->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.14	</version>
    <!-- 排除slf4j-log4j12，否则会出错 -->
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>

```



在provider配置注册中心地址，服务应用名称，扫描的包

dubbo-spring.properties:

```properties
dubbo.application.name=my_provider
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
```

开启Dubbo，扫描service包，导入dubbo配置，也可以单独使用一个配置类配置

```java
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.example.service")
@PropertySource("classpath:dubbo-spring.properties")
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}

}
```

编写service

```java
public interface TicketService {
    public String getTicket();
}

@Component
@Service		// 这个不是spring的service，是dubbo的service
public class TicketServiceImpl implements TicketService {
    @Override
    public String getTicket() {
        return "a ticket";
    }
}
```

> `@Service`是这个service：org.apache.dubbo.config.annotation.Service

provider就完成了，运行jar包就会自动去注册中心注册了



配置consumer

dubbo-spring.properties

```properties
dubbo.application.name=my_consumer
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.consumer.timeout=3000
```

编写配置类导入配置

```java
@Configuration
@EnableDubbo(scanBasePackages = "com.example.service")
@PropertySource("classpath:dubbo-spring.properties")
public class ConsumerConfig {
}
```

> 要使用远程服务，就要有远程服务的接口（TicketService），但是consumer没有，有两种方法解决
>
> 1. 导入远程的pom坐标
> 2. 在本地同名的包下创建相同的接口
>
> 这里使用第二种方法

```java
public interface TicketService {
    public String getTicket();
}
```

编写本地service

```java
@Service		// 这是spring的service
public class UserService {

    @Reference
    private TicketService ticketService;

    public void buyTicket() {
        String ticket = ticketService.getTicket();
        System.out.println("在注册中心拿到=>" + ticket);

    }

}
```

注入远程的service使用`@Reference`注解



测试

```java
@SpringBootTest
class ConsumerApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		userService.buyTicket();
	}

}
```

