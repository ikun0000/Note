# IDEA community配置



## 插件

>  [IntelliJ Idea 常用10款插件（提高开发效率），附优秀主题插件](https://blog.csdn.net/weixin_41846320/article/details/82697818)
>
> [Java软件开发利器：IDEA插件分享!以Spring Boot项目为载体进行视频教程演示](https://www.bilibili.com/video/av95417273/)

1.   Database Navigator：数据库管理
2.  Spring Assistant：SpringBoot初始化器
8.   JBLHtmlToThymeleaf：Thymeleaf插件（在 IDEA 目录树中可以右击html 文件（只有选择html文件右击才会出现对于菜单），然后点击 JBLHtmlToThymeleaf 可以在html页面中的 html标签中添加属性及值：xmlns:th="http://www.thymeleaf.org" bug 或问题请邮件。）
10.   Springirun：提供spring配置文件支持
13.   LazySpring：用于类/测试依赖项的ontext-config生成。 对于使用Spring Beans进行JUnit-tests中的快速依赖关系解析很有用。
16.   RestfulToolkit：一套 RESTful 服务开发辅助工具集。
19.   J2EECfgFile：生成J2EE配置文件
24.   Smart Tomcat：社区版的野生tomcat
25.   Free MyBatis plugin：生成mapper xml文件，快速从代码跳转到mapper及从mapper返回代码，mybatis自动补全及语法错误提示，集成mybatis generator gui界面
10.   Lombok：使用Lombok时不报错
11.   CodeGlance：右侧展示代码地图
12.   Translation：IDEA翻译插件
13.   Rainbow Brackets：彩虹色括号标记
14.   Grep Console：日志着色插件
15.   Statistic：代码统计插件
16.   Alibaba Java Coding Guidelines：阿里巴巴开发规范 
17.   jclasslib Bytecod viewer：展示字节码指令
18.   Alibaba Cloud Toolkit：Alibaba开发套件（直接连接阿里云，ssh工具等）
19.   MybatisCodeHelperPro：Mybatis支持
20.   SequenceDiagram：画时序图
21.   codehelper.generator：代码生成



## Settings配置

Settings -> Editor -> Font -> Size：修改字体大小为15

Settings -> Editor -> Code Style -> General -> Hard warp at：设置为120

Settings -> Editor -> File and Code Templates -> File：添加JSP Template

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title></title>
</head>
<body>

</body>
</html>        
```

Settings -> Editor -> File Encoding：设置UTF-8

Settings -> Editor -> Live Template：添加自己的关键字生成代码，先添加一类myKey：

bootstrap

```html
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap 101 Template</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
    <!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
    <!--[if lt IE 9]>
      <script src="https://cdn.jsdelivr.net/npm/html5shiv@3.7.3/dist/html5shiv.min.js"></script>
      <script src="https://cdn.jsdelivr.net/npm/respond.js@1.4.2/dest/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <h1>你好，世界！</h1>

    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
  </body>
</html>
```

cjdbc：

```java
com.mysql.cj.jdbc.Driver
```

jdbc：

```java
com.mysql.jdbc.Driver
```

getthreadname：

```java
Thread.currentThread().getName()
```

newthread：

```java
new Thread(() -> {
    
}, "$THREAD_NAME$").start();

```

secondsleep：

```java
try { TimeUnit.SECONDS.sleep($SECOND$); } catch (InterruptedException e) { e.printStackTrace(); }
```

servlet：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
  version="4.0"
  metadata-complete="true">

  <!-- 配置SpringMVC的转发器，这个东西会转发对应的请求到对应的controller -->
  <servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <!-- 配置spring的context文件 -->
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

  <!-- 配置编码过滤器，处理乱码问题 -->
  <filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>encoding</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

</web-app>

```

springbean：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:c="http://www.springframework.org/schema/c"
    xmlns:util="http://www.springframework.org/schema/util"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xmlns:mvc="http://www.springframework.org/schema/mvc"   
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">
    
	

</beans>
```

thread：

```java
// 判断
while ($expression$) {
    this.wait();
}

// 操作


// 通知
this.notifyAll();

```

trylock：

```java
$LOCK$.lock();
try {
    
} catch (Exception e) {
    e.printStackTrace();
} finally {
    $LOCK$.unlock();
}
```



## IDEA识别Maven项目卡死

IDEA在识别Mave项目时卡死，包括使用Spring项目生成器生成项目后卡死，右下角一直显示 `Reading Maven project` ，但是一点没动

解决方法：

找到 `C:\Windows\System32\Driver\etc\host` 

把 `127.0.0.1 localhost` 的注释去掉

把 `::1 localhost` 的注释去掉

以管理员的身份打开cmd，执行

```powershell
netsh winsock reset
```

然后重启，再次导入会没问题了



## IDEA无法显示Project中的src目录

使用SpringBoot初始化器初始化项目后IDEA中project栏中没有src目录。

解决方法：

关闭IDEA，删除项目的打开记录

然后去到项目所在的目录

把 `.idea` 目录删除

打开IDEA并重新导入这个项目