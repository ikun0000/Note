# Spring整合MyBatis

## 方法1

1. 导入依赖

   ```xml
   <dependency>
       <groupId>junit</groupId>
       <artifactId>junit</artifactId>
       <version>4.13</version>
   </dependency>
   
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.19</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-jdbc</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>org.mybatis</groupId>
       <artifactId>mybatis</artifactId>
       <version>3.5.4</version>
   </dependency>
   
   <dependency>
       <groupId>org.mybatis</groupId>
       <artifactId>mybatis-spring</artifactId>
       <version>2.0.3</version>
   </dependency>
   
   <dependency>
       <groupId>org.aspectj</groupId>
       <artifactId>aspectjweaver</artifactId>
       <version>1.9.5</version>
   </dependency>
   
   <dependency>
       <groupId>org.aspectj</groupId>
       <artifactId>aspectjrt</artifactId>
       <version>1.9.5</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring</artifactId>
       <version>5.2.3.RELEASE</version>
       <type>pom</type>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-core</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-beans</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-aop</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-web</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-webmvc</artifactId>
       <version>5.2.4.RELEASE</version>
   </dependency>
   ```

2. 编写实体类

   ```java
   public class Staff {
       private Long id;
       private String name;
       private String sex;
       private Integer age;
       private Integer depId;
       // get set toString
   }
   ```
   
3. 编写接口

   ```java
   public interface StaffMapper {
       List<Staff> findAll();
   }
   ```

4. 编写mybatis配置文件（mybatis-config.xml）

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
   
       <settings>
           <setting name="mapUnderscoreToCamelCase" value="true"/>
       </settings>
       
       <mappers>
           <mapper resource="mappers/staffmapper.xml"/>
       </mappers>
   </configuration>
   ```

5. 编写mapper文件

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="org.example.mapper.StaffMapper">
   
       <select id="findAll" resultType="org.example.pojo.Staff">
           select * from staff
       </select>
   
   </mapper>
   ```

6. 配置applicationContext.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:c="http://www.springframework.org/schema/c"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/aop
           https://www.springframework.org/schema/aop/spring-aop.xsd">
   
       <!-- 配置数据源(c3p0, dbcp, druid)，这里使用spring的数据源 -->
       <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
           <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
           <property name="url" value="jdbc:mysql://10.10.10.246:3306/school"></property>
           <property name="username" value="root"></property>
           <property name="password" value="123456"></property>
       </bean>
   
       <!-- 配置SqlSessionFactory -->
       <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
           <property name="dataSource" ref="dataSource" />
           <!-- 绑定mybatis config配置文件 -->
           <property name="configLocation" value="classpath:mybatis-config.xml"></property>
       </bean>
   
       <!-- SqlSessionTemplate就是SqlSession -->
       <bean id="sqlTemplate" class="org.mybatis.spring.SqlSessionTemplate">
           <!-- 这个类没有set方法，只能通过构造器注入 -->
           <constructor-arg index="0" ref="sqlSessionFactory"></constructor-arg>
       </bean>
   
   </beans>
   ```

7. 添加Mapper实现

   ```java
   public class StaffMapperImpl implements StaffMapper {
   
       private SqlSessionTemplate sqlSessionTemplate;
   
       public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
           this.sqlSessionTemplate = sqlSessionTemplate;
       }
   
       @Override
       public List<Staff> findAll() {
           StaffMapper mapper = sqlSessionTemplate.getMapper(StaffMapper.class);
           return mapper.findAll();
   
       }
   }
   ```

8. 添加上一步的实现类到bean

   ```xml
   <bean id="staffMapper" class="org.example.mapper.StaffMapperImpl">
       <property name="sqlSessionTemplate" ref="sqlTemplate"></property>
   </bean>
   ```

9. 测试

   ```java
       @Test
       public void testMybatis() throws IOException {
   //        String resource = "mybatis-config.xml";
   //        InputStream in = Resources.getResourceAsStream(resource);
   //        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
   //        SqlSession sqlSession = sqlSessionFactory.openSession(true);
   //        StaffMapper mapper = sqlSession.getMapper(StaffMapper.class);
   //
   //        List<Staff> staffList = mapper.findAll();
   //
   //        System.out.println(staffList);
           ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
           StaffMapper userMapper = (StaffMapper) context.getBean("staffMapper");
   
           List<Staff> all = userMapper.findAll();
           for (Staff staff : all) {
               System.out.println(staff);
           }
   ```




## 方法2

1. 导入以来（看上面）

2. 在方法1.7处添加Mapper实现额外集成SqlSessionDaoSupport

   ```java
   public class StaffMapperImpl extends SqlSessionDaoSupport implements StaffMapper {
   
       @Override
       public List<Staff> findAll() {
           return getSqlSession().getMapper(StaffMapper.class).findAll();
       }
   }
   ```

3. 在applicationContext.xml中添加这个Mapper实现

   ```xml
   <bean id="staffMapper" class="org.example.mapper.StaffMapperImpl">
       <property name="sqlSessionFactory" ref="sqlSessionFactory"></property>
   </bean>
   ```

4. 测试



# 事务

## 声明式事务(下面的和官方的配置不同)

1. 配置TransactionManager

   ```xml
   <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
     <constructor-arg ref="dataSource" />
   </bean>
   ```

2. 结合AOP实现事务织入（配置事务通知），配置事务传播

   ```xml
   <tx:advice id="txAdvice" transaction-manager="transactionManager">
       <tx:attributes>
           <tx:method name="findAll"/>
       </tx:attributes>
   </tx:advice>
   ```

   可以在`<tx:attributes>`添加开启事务的方法

   ```xml
   <tx:method name="findByName" read-only="true"/>
   <tx:method name="add" propagation="REQUIRED"/>
   <tx:method name="delete"/>
   <tx:method name="update"/>
   <tx:method name="*"/>
   ```

   `propagation="REQUIRED"`是默认选项（spring的七种事务之一）

3. 配置事务切入

   ```xml
   <aop:config>
       <aop:pointcut id="txPointCut" expression="execution(* org.example.mapper.*.*(..))"/>
       <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointCut"></aop:advisor>
   </aop:config>
   ```

   

## 编程式事务

[参考]( http://mybatis.org/spring/zh/transactions.html )