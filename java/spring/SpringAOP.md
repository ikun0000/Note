# AspectJ

 #### AOP概念名词

AOP（面向切面编程），简单来说就是执行垂直于业务逻辑，所有业务逻辑都是平行的执行，而AOP则垂直于一个或多个业务逻辑。也就是说，当被切过的业务逻辑执行到相交的点的时候会先或者后执行切面的代码。可以理解为交点就是一个事件，当触发了这个事件就会去执行AOP代码，至于是先执行还是后执行取决于实际编码

| 名称                      | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| 切面（Aspect）            | 一个关注点的模块化，这个关注点可能会横切多个对象             |
| 连接点（Joinpoint）       | 程序执行过程中的某个特定的点                                 |
| 通知（Advice）            | 在切面的某个特定的连接点上执行的动作                         |
| 切入点（Pointcut）        | 匹配连接点的断言，在AOP中通知和一个切入点表达式关联          |
| 目标对象（Target Object） | 被一个或多个切面所通知的对象                                 |
| AOP代理（AOP proxy）      | AOP框架创建的对象，用来实现切面契约（aspect contract）（包括通知方法执行等功能） |
| 织入（Weaving）           | 把切面连接到其它应用程序类型或者对象上，并创建一个被通知的对象，分为：编译时织入，类加载时织入，执行时织入 |
| 引入（Introduction）      | 在不修改类代码的前提下，为类添加新的方法和属性               |



#### Advice类型

| 名称                                    | 说明                                                         |
| --------------------------------------- | ------------------------------------------------------------ |
| 前置通知（Before advice）               | 在某个连接点***之前执行***的通知，它不能阻止连接点的执行，除非通知本身有异常 |
| 返回后通知（After returning advice）    | 在连接点***正常返回***后的通知                               |
| 抛出异常后通知（After throwing advice） | 在方法***抛出异常***退出时执行的通知                         |
| 后通知（After(finally) advice）         | 当某个连接点退出时执行的通知，***无论时正常返回还是异常退出*** |
| 环绕通知（Around advice）               | 包围一个连接点的通知                                         |



### 切点配置

这个是表示当那些包，那些包的类，和类的方法执行时执行的AOP代码



```
execution(public * *(..))
```

切入点为执行所有public方法时

```
execution(* set*(..))
```

切入点为执行所有set开始的方法时

```
execution(* com.xyz.service..(..))
```

切入点为com.xyz.service包下所有的方法不包括子包的方法时

```
execution(* com.xyz.service...(..))
```

切入点为com.xyz.service包下所有的方法包括子包的方法时

```
execution(* com.xyz.service.Bean1.*(..))
```

切入点为com.xyz.service.Bean1类下的所有方法

```
execution(* com.xyz.service.Bean1.aaa(..))
```

切入点为com.xyz.service.Bean1类的aaa方法，括号中的..指定参数，这里意思是不匹配参数



### 使用AOP的依赖配置

```xml
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-aop</artifactId>
<version>5.2.2.RELEASE</version>
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
```



### Spring实现AOP

#### 方法1

业务类

```java
public interface UserService {
    void add();
    void delete();
    void update();
    void query();
}

public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("add user");
    }

    @Override
    public void delete() {
        System.out.println("delete user");
    }

    @Override
    public void update() {
        System.out.println("update user");
    }

    @Override
    public void query() {
        System.out.println("query user");
    }
}
```

前置通知类：

```java
public class Log implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName() + "   :   " + method.getName());
    }
}
```

后置通知类：

```java
public class AfterLog implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("return " + method.getName() + " return value  " + returnValue);
    }
}
```

  applicationContext.xml

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

    <!-- 注册bean -->
    <bean id="userService" class="org.example.service.UserServiceImpl"></bean>
    <bean id="afterLog" class="org.example.log.AfterLog"></bean>
    <bean id="log" class="org.example.log.Log"></bean>

<!--     配置AOP -->
    <aop:config>
        <!-- execution(修饰符 返回值 类名 方法名 (参数)) -->
        <aop:pointcut id="pc" expression="execution(* org.example.service.UserServiceImpl.*(..))"/>
        <!-- 执行环绕 -->
        <aop:advisor advice-ref="log" pointcut-ref="pc"></aop:advisor>
        <aop:advisor advice-ref="afterLog" pointcut-ref="pc"></aop:advisor>
    </aop:config>

</beans>
```



#### 方法2(注册切面)

通知类：

```java
public class MyAdvice {
    void before() {
        System.out.println("=========before=========");
    }

    void after() {
        System.out.println("=========after=========");
    }
}
```

applicationContext.xml

```xml
<bean id="myAdvice" class="org.example.log.MyAdvice"></bean>

<aop:config>
    <aop:aspect ref="myAdvice">
        <aop:pointcut id="pc" expression="execution(* org.example.service.UserServiceImpl.*(..))"/>
        <aop:before method="before" pointcut-ref="pc"></aop:before>
        <aop:after-returning method="after" pointcut-ref="pc"></aop:after-returning>
    </aop:aspect>
</aop:config>
```





### 注解实现

Configuration定义

```java
package org.czk;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("org.czk.targetobject")
@ComponentScan("org.czk.aspectclass")
@EnableAspectJAutoProxy
public class MyConfiguration {
}
```

Bean定义

```java
package org.czk.targetobject;

import org.springframework.stereotype.Component;

@Component
public class Bean1 {

    public void aaa() {
        System.out.println("aaa exec");
    }

    public int bbb() {
        System.out.println("bbb exec");
        return 5;
    }

    public void div() throws Exception {
        throw new Exception("have exception");
    }

    public void around() {
        System.out.println("arround");
    }

}
```

Aspect定义

```java
package org.czk.aspectclass;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {
    @Pointcut("execution(* org.czk.targetobject.Bean1.aaa(..))")        // 定义一个切点
    public void aaa() {
    }

    @Before("aaa()")                // 直接引用切点，在返回调用前通知
    public void beforeAaa() {
        System.out.println("before aaa()");
    }

    @AfterReturning("aaa()")        // 在返回后通知
    public void afteraaa() {
        System.out.println("after running aaa()");
    }

    @AfterReturning(pointcut = "execution(* org.czk.targetobject.Bean1.bbb(..))", returning = "retVal")
    // 返回后通知，并且获取返回值
    public void bbbAfterReturning(Object retVal) {
        System.out.println("return value: " + retVal.toString());
    }

    @AfterThrowing(pointcut = "execution(* org.czk.targetobject.Bean1.div(..))", throwing = "ex")
    // 异常时执行，并且获取抛出的异常
    public void afterException(Object ex) {
        System.out.println("have execption");
        System.out.println(ex.toString());
    }

    @After("execution(* org.czk.targetobject.Bean1.*(..))")     // 所有方法返回执行
    public void allAfter() {
        System.out.println("all function return");
    }

    @Around("execution(* org.czk.targetobject.Bean1.around(..))")           // 环绕通知
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("around before");
        Object retVal = proceedingJoinPoint.proceed();                  // 这里真正调用要执行的方法，如果有参数以Object[]传入
        // proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        System.out.println("around after");
        return retVal;
    }
}
```



