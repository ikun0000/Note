# SpringBoot集成Druid

#### 导入依赖

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.21</version>
</dependency>
```



数据源常用配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://10.10.10.246:3306/jdbc
    username: root
    password: 123456
    type: com.alibaba.druid.pool.DruidDataSource    # 使用druid
    druid:		# druid配置
      initial-size: 8
      min-idle: 1
      max-active: 20
      max-wait: 60000
      time-between-eviction-runsMillis: 60000
      min-evictable-idle-timeMillis: 300000
      validation-query: select 'x' FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: true
      max-open-prepared-statements: 20
      max-pool-prepared-statement-per-connection-size: 20
      filters: stat
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
      use-global-data-source-stat: true
```

[所有属性说明]( https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter )



配置后台

都是死配置

```java
package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDatasource() {
        return new DruidDataSource();
    }

    // 后台监控
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");   // 配置堵路的url，注意：是一个*

        // 设置后台账号密码
        // 设置初始化参数
        HashMap<String, String> initParam = new HashMap<>();
        initParam.put("loginUsername", "admin");        // 登陆的key，key固定
        initParam.put("loginPassword", "123456");

        // 允许谁访问
        initParam.put("allow", "localhost");

        bean.setInitParameters(initParam);
        return bean;
    }

}
```

