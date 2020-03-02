# SpringBean配置

### 添加依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.2.2.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.2.2.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
    <version>5.2.2.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>5.2.2.RELEASE</version>
</dependency>

<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>

<dependency>
    <groupId>javax.inject</groupId>
    <artifactId>javax.inject</artifactId>
    <version>1</version>
</dependency>
```







### XML配置Bean

使用maven创建项目，一般新建的话在src/main下是没有resource目录的，自己建一个，里面的xml文件用来配置Bean

Bean的xml基本配置

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

	<!-- 这里写自己的Bean -->

</beans>
```





#### 普通类的Bean配置

在src下面建好包然后创建一个类

```java
package org.czk;

public class People {
    protected String name;
    protected int age;


    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void info() {
        System.out.println("Name: " + this.name);
        System.out.println("Age: " + this.age);
    }
}

```

然后在xml文件中配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:p="http://www.springframework.org/schema/p"
               xmlns:util="http://www.springframework.org/schema/util"
               xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
        http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.1.xsd">

    <beans>
        <bean id="people" class="org.czk.People">
            <constructor-arg name="name" type="String" value="aaa"></constructor-arg>
            <constructor-arg name="age" type="int" value="1"></constructor-arg>

            <property name="name" value="aaa"></property>
            <property name="age" value="1"></property>
        </bean>
    </beans>

</beans>
```

这样就可以把People类交给IoC容器管理



```xml
<constructor-arg name="name" type="String" value="aaa"></constructor-arg>
<constructor-arg name="age" type="int" value="1"></constructor-arg>
```

这个标签用来使用构造器初始化类，name是参数名，type要指定类型，如果是简单数据类型用value初始化值，复杂类型或者用其他bean初始化用ref



```xml
<property name="name" value="aaa"></property>
<property name="age" value="1"></property>
```

这个标签用来使用set方法初始化bean，name和value和构造器的一样



然后如果要使id值来获取Bean

```java
package org.czk;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("springbean.xml");	// 获取应用程序上下文，使用xml配置的话要实例出一个ClassPathXmlApplicationContext对象

        People p = context.getBean("people", People.class);		// 这样就可以向IoC容器获得一个实例了

        p.info();
    }
}

```





#### 继承关系的Bean的配置

在上面的基础上加多一个类继承People类

```java
package org.czk;

public class Student extends People {
    private String id;


    public Student(String name, int age, String id) {
        super(name, age);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void info() {
        System.out.println("Student ID: " + this.id);
        super.info();
    }
}

```



xml的配置改成如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:p="http://www.springframework.org/schema/p"
               xmlns:util="http://www.springframework.org/schema/util"
               xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
        http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.1.xsd">

    <beans>
        <bean id="people" class="org.czk.People">
            <constructor-arg name="name" type="String" value="aaa"></constructor-arg>
            <constructor-arg name="age" type="int" value="1"></constructor-arg>

            <property name="name" value="aaa"></property>
            <property name="age" value="1"></property>
        </bean>

        <bean id="student" class="org.czk.Student" parent="people">
            <constructor-arg name="id" type="String" value="111111111"></constructor-arg>

            <property name="id" value="111111111"></property>
        </bean>

    </beans>

</beans>
```



要改的就是将bean标签加上parent，并把它指向父类bean的id

```xml
<bean id="student" class="org.czk.Student" parent="people">
    <constructor-arg name="id" type="String" value="111111111"></constructor-arg>

    <property name="id" value="111111111"></property>
</bean>
```





#### 工厂模式初始化Bean

创建一个工厂类

```java
package org.czk;

import java.util.List;

public class HumanFactory {

    public static People getPeople() {
        return new People("aaa", 100);
    }

    public static Student getStudent() {
        return new Student("bbb", 22, "22222222");
    }

}
```

在xml中加上

```xml
<bean id="peoplepeople" class="org.czk.HumanFactory" factory-method="getPeople"></bean>
<bean id="studentstudent" class="org.czk.HumanFactory" factory-method="getStudent"></bean>
```





#### 动态工厂初始化Bean

创建动态工厂

```java
package org.czk;

import java.util.List;

public class HumanFactory {

    public People getPeople() {
        return new People("aaa", 100);
    }

    public Student getStudent() {
        return new Student("bbb", 22, "22222222");
    }

}
```

xml配置

```xml
<bean id="factory" class="org.czk.HumanFactory"></bean>
<bean id="peoplepeople" factory-bean="factory" factory-method="getPeople"></bean>
<bean id="studentstudent" factory-bean="factory" factory-method="getStudent"></bean>
```





#### 复杂数据类型的Bean配置

定义一个类来包含Student类的列表

```java
package org.czk;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private List<Student> allStudent;
    private String className;

    public Class(List<Student> allStudent, String className) {
        this.allStudent = allStudent;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addStudent(Student student) {
        allStudent.add(student);
    }

    public void removeStudent(Student student) {
        allStudent.remove(student);
    }

    public List<Student> getAllStudent() {
        return allStudent;
    }

    @Override
    public String toString() {
        return "Class{" +
                "allStudent=" + allStudent +
                ", className='" + className + '\'' +
                '}';
    }
}

```



在上面xml文件中的beans加上

```java
<bean id="claxx" class="org.czk.Class">
    <constructor-arg name="className" type="String" value="class 1"></constructor-arg>
        <constructor-arg name="allStudent" ref="student"></constructor-arg>

        <property name="className" value="class 1"></property>
</bean>
```

或者

```xml
<bean id="claxx" class="org.czk.Class">
    <constructor-arg name="className" value="Class B"></constructor-arg>
    <constructor-arg name="allStudent">
        <list>
            <ref bean="student"></ref>
            <ref bean="student"></ref>
            <ref bean="student"></ref>
            <ref bean="student"></ref>

        </list>
    </constructor-arg>

</bean>
```

这样可以按顺序初始化List，对于List里面的是复杂数据类型用ref标签指向用哪个Bean来初始化，对于简单数据类型使用value标签直接初始化`<value>aaaaa</value>`

Set和List一样有对应的set标签

对于Map类型，里面包含的是entry，entry标签用key，ke'y-ref属性初始化key，用value，value-ref来初始化value，简单数据类型的用key，value属性，复杂数据类型的用带ref的指向要初始化的bean





#### Bean别名

这样可以用student2来获得Student的实例，name指向要取别名的Bean的id

```xml
<alias name="student" alias="student2"></alias>
```





#### Bean作用域

在bean标签加上scope属性

```xml
<bean id="people" class="org.czk.People" scope="singleton">
    <constructor-arg name="name" type="String" value="aaa"></constructor-arg>
    <constructor-arg name="age" type="int" value="1"></constructor-arg>

    <property name="name" value="aaa"></property>
    <property name="age" value="1"></property>
</bean>

<bean id="student" class="org.czk.Student" parent="people" scope="prototype">
    <constructor-arg name="id" type="String" value="111111111"></constructor-arg>

    <property name="id" value="111111111"></property>
</bean>
```

> singleton 代表创建这个bean的单例模式，意思就是在整个程序中只实例化一次，每次getBean都会返回同一个实例（hashcode一样）

> prototype 与singleton相反，它创建的是多例模式，每一次getBean都会创建一个bean的实例

 



#### Web作用域

> request 
>
> session
>
> application
>
> websocket





#### 自定义作用域

自定i一个scope实现双例模式

实现一个自定义scope需要继承**org.springframework.beans.factory.config.Scope**类

```java
package org.czk;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryScope implements Scope {
    Map<String, Object> map1 = new ConcurrentHashMap<String, Object>();
    Map<String, Object> map2 = new ConcurrentHashMap<String, Object>();


    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        if (!map1.containsKey(s)) {
            Object o = objectFactory.getObject();
            map1.put(s, o);
            return o;
        }

        if (!map2.containsKey(s)) {
            Object o = objectFactory.getObject();
            map2.put(s, o);
            return o;
        }

        if ( (new Random()).nextBoolean()) {
            return map1.get(s);
        } else {
            return map2.get(s);
        }
    }

    @Override
    public Object remove(String s) {
        if (map1.containsKey(s)) {
            Object o = map1.get(s);
            map1.remove(s);
            return o;
        }
        if (map2.containsKey(s)) {
            Object o = map2.get(s);
            map2.remove(s);
            return o;
        }

        return null;
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
```

xml配置

```xml
<bean id="binaryScope" class="org.czk.BinaryScope"></bean>

<bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
    <property name="scopes">
        <map>
            <entry key="binaryScope" value-ref="binaryScope"></entry>
        </map>
    </property>
</bean>
```





#### Bean懒加载

在bean标签加上`lazy-init="true"`，或者在beans标签加上`default-lazy-init="true"`，之后所有的bean都是懒加载模式

```xml
<bean id="student" class="org.czk.Student" parent="people" scope="prototype" lazy-init="true">
    <constructor-arg name="id" type="String" value="111111111"></constructor-arg>

    <property name="id" value="111111111"></property>
</bean>
```

设置了懒加载模式之后只有遇到getBean才会实例化一个bean对象





#### Bean初始化和销毁

在bean标签加上`init-method="initial" destroy-method="destory"`设置初始化和销毁时执行的方法

如果所有的bean都有同名的初始化/销毁方法可以在beans标签设置`default-init-method` 和 `default-destroy-method`

```xml
<bean id="people" class="org.czk.People" scope="binaryScope" init-method="initial" destroy-method="destory">
    <constructor-arg name="name" type="String" value="aaa"></constructor-arg>
    <constructor-arg name="age" type="int" value="1"></constructor-arg>

    <property name="name" value="aaa"></property>
    <property name="age" value="1"></property>
</bean>
```

显示销毁

```java
package org.czk;


import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        AbstractApplicationContext context =
                new ClassPathXmlApplicationContext("springbean.xml");

        People people = context.getBean("people", People.class);
        people.info();

        context.close();
    }
}
```





#### 开启注解开发

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:c="http://www.springframework.org/schema/c"
    xmlns:util="http://www.springframework.org/schema/util"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config></context:annotation-config>

</beans>
```





### Annotation配置Bean

首先要创建一个管理Bean的类，相当于xml文件的作用

```java
package org.czk;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public People people() {
        return new People("aaa", 1);
    }

    @Bean(value = "stu")
    public Student student() {
        return new Student("bbb", 12, "123456789");
    }
}
```

要设置Bean别名将`@Bean`标签的value传入`{'name1', 'name2', ...}`



#### 使用@Autowired

require设置为false允许属性为null

@Qualifier显示指定唯一的bean

```java
public class Human {

    @Autowired(required = false)
    private Cat cat;
    @Autowired
    @Qualifier(value = "dog222")
    private Dog dog;
    private String name;
 	// get set toString   
}
```





然后可以在Test中使用Bean

```java
package org.czk;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfiguration.class);

        People people = context.getBean("people", People.class);
        Student student = context.getBean("stu", Student.class);
        people.info();
        student.info();
    }
}
```



#### 简化配置

在每个bean类加上`@Component`注解

```java
package org.czk;

import org.springframework.stereotype.Component;

@Component
public class Bean1 {
}
```

>@Component
>
>@Repository
>
>@Service
>
>@Controller
>
>这四个注解都是一样的，都是注册到spring容器中
>

在bean的配置类加上`@ComponentScan`这里指向存放Bean的包

```java
package org.czk;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "org.czk")
public class MyConfiguration {
}
```

> 使用@Import(配置类)可以导入其他配置类

使用三种方法注入字段值

```java
@Component
public class Bean1 {
    @Autowired
    String string1;

    public Bean1(String string1) {
        this.string1 = string1;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
}
```

```java
@Component
public class Bean1 { 
    String string1;

    @Autowired
    public Bean1(String string1) {
        this.string1 = string1;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
}
```

```java
@Component
public class Bean1 {
    String string1;
 
    public Bean1(String string1) {
        this.string1 = string1;
    }

    public String getString1() {
        return string1;
    }

    @Autowired
    public void setString1(String string1) {
        this.string1 = string1;
    }
}
```

Configuration类要加上生成String的bean

> 默认情况下使用@Bean注解配置的bean的bean id是方法名

```java
@Configuration
@ComponentScan(value = "org.czk")
public class MyConfiguration {

    @Bean
    public String string() {
        return "aaaaa";
    }
}
```

使用简单数据类型也可以用`@Value`来初始化

```java
@Component 
public class Bean1 {
    @Value("bbbb")
    String string1;

    public Bean1(String string1) {
        this.string1 = string1;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
}
```





集合类型的数据注入

```java
@Component
public class Bean1 {
    @Autowired
    List<String> stringList;

    @Override
    public String toString() {
        return "Bean1{" +
                "stringList=" + stringList +
                '}';
    }
}
```



生成多个list元素

```java
@Configuration
@ComponentScan(value = "org.czk")
public class MyConfiguration {

    @Bean
    @Order(24)
    public String string1() {
        return "aaaaa";
    }

    @Bean
    @Order(50)
    public String string2() {
        return "bbbbbb";
    }
}
```

或者一个List搞定

```java
@Configuration
@ComponentScan(value = "org.czk")
public class MyConfiguration {

    @Bean
    public List<String > stringList() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("ccccc");
        stringList.add("dddddd");

        return stringList;
    }
}
```



> 如果时Map的话写一个返回Map的Bean即可
>
> 或者时返回Map的key的bean和返回value的bean



#### 设置作用域

在类上加上`@Scope`标签设置`singleton` 或者 `prototype`

```java
@Repository
@Scope("singleton")
public class User {
    @Value("aaa")
    private String name;
	// get set
}
```



首先先继承`**org.springframework.beans.factory.config.Scope**`创建自己的规则

在Configuration类加上`**CustomScopeConfigurer**`的Bean

```java
package org.czk;


import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(value = "org.czk")
public class MyConfiguration {

    @Bean
    public List<String > stringList() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("ccccc");
        stringList.add("dddddd");

        return stringList;
    }

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer =
                new CustomScopeConfigurer();
        customScopeConfigurer.addScope("binaryScope", new BinaryScope());

        return customScopeConfigurer;
    }
}

```





#### 懒加载

在bean类上加上`@Lazy`即可实现懒加载，在Configuration类加则所有Bean实现懒加载



#### Bean初始化和销毁

```java
@PostConstruct
public void initial() {
    // initial code
}
    
@PreDestroy
public void destroy() {
    // destroy code
}
```



#### 导入其他beans的配置

applicationContext.xml

```
<import resource="./beans.xml"></import>
```



#### 配置各种类型bean大杂烩

Address

```java
public class Address {
    private String address;
	// get set toString
}
```

Student

```java
public class Student {
    private String name;
    private Address address;
    private String[] books;
    private List<String> hobbys;
    private Map<String, String> card;
    private Set<String> games;
    private Properties info;
    private String wife;
	// get set toString
}
```

applicationContext.xml

```xml
<bean id="address" class="org.example.pojo.Address" scope="prototype">
        <property name="address" value="天桥底"></property>
    </bean>

    <bean id="student" class="org.example.pojo.Student" scope="prototype">
        <property name="name" value="测试姓名"></property>

        <property name="address" ref="address"></property>

        <property name="books">
            <array>
                <value>大学物理</value>
                <value>高等数学</value>
                <value>专业英语</value>
            </array>
        </property>

        <property name="wife">
            <null></null>
        </property>

        <property name="card">
            <map>
                <entry key="校园卡" value="50元"></entry>
                <entry key="饭卡" value="500元"></entry>
                <entry key="公交卡" value="100元"></entry>
            </map>
        </property>

        <property name="hobbys">
            <list>
                <value>唱</value>
                <value>跳</value>
                <value>rep</value>
                <value>篮球</value>
            </list>
        </property>

        <property name="games">
            <set>
                <value>王者荣耀</value>
                <value>GTA</value>
                <value>吃鸡</value>
                <value>老滚5</value>
                <value>彩6</value>
            </set>
        </property>

        <property name="info">
            <props>
                <prop key="年级">大一</prop>
            </props>
        </property>
    </bean>
```



#### 使用p(properties)命名空间和c(constructor)命名空间注入属性值

User

```java
public class User {
    private String name;
    private int age;
	// NoArgsConstractor AllArgsConstractor get set toString
}
```



userbeans.xml

`p`后面跟着属性名，`c`后面跟着构造函数的参数名

```xml
<bean id="user" class="org.example.pojo.User" p:name="aaa" p:age="26"></bean>
<bean id="userc" class="org.example.pojo.User" c:name="bbb" c:age="22"></bean>
```





#### Bean自动装配

autowire

> byName: 在上下文寻找和set方法名匹配的bean并自动装配
>
> byType: 在上下文寻找和set类型匹配的bean并自动装配

pojo

```java
class Cat {
    public void say() {
        System.out.println("miao");
    }
}

class Dog {
    public void say() {
        System.out.println("wang");
    }
}

class Human {
    private Cat cat;
    private Dog dog;
    private String name;
	// get set toString
}
```



applicationContext.xml

```xml
<bean id="cat" class="org.example.pojo.Cat"></bean>
<bean id="dog" class="org.example.pojo.Dog"></bean>
<bean id="human" class="org.example.pojo.Human" autowire="byName">
    <property name="name" value="aa"></property>
</bean>
```

