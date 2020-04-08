# Maven基本用法

### 安装

1. 从[https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)下载Maven并解压到想要安装的目录
2. 打开apache-maven-*/conf/settings.xml，找到`<localRepository>D:\apache-maven-3.6.3</localRepository>`把它复制出来并且把把标签中间的东西改成安装Maven的目录
3. 设置环境变量`M2_HOME`和`MAVEN_HOME`为Maven的安装目录
4. 设置`PATH`环境变量为`MAVEN_HOME`目录下的bin目录
5. 搞定



### 设置Maven远程仓库为Aliyun仓库

在`MAVEN_HOME`目录下的conf目录下的settings.xml文件中找到mirrors那一块（默认是注释）在那个位置加上下面这个，复制`<mirrors></mirrors>`里面的就行，**加s的代表镜像源集合，不加s的就是其中一个镜像源，下面有好多标签类似这样**

```xml
<mirrors>
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        
    </mirror>
</mirrors>
```



### Maven依赖查询

[ https://mvnrepository.com/ ]( https://mvnrepository.com/ )



### Maven目录结构

```
Maven project
|	Artifact01									// 表示artifactId
|	|	src										// Maven项目的源代码
|	|	|	main								// 这个下面放项目代码
|	|	|	|	java
|	|	|	|	|	pkg							// 这里就是按着groupId起包了
|	|	|	test								// 这个下面是测试代码
|	|	|	|	java
|	|	|	|	|	pkg
|	|	|	resource							// 这里存放资源文件
|	|	target									// compile、package之后的东西
|	|	pom.xml									// Maven项目的配置文件
|	Artifact02
|	|
```



### Mave基本命令

查看Maven信息

```
# mvn -v
```

创建Maven项目

```
# mvn archetype:generate
上面是交互式的，或者
# mvn archtype:generate -DgroupId=<组织名，公司网址反写+项目名>
						-DartifactId=<项目名-模块名>
						-Dversion=<版本号>
						-Dpackage=<代码所存在的包名>
```

编译Maven项目

```
# mvn compile
```

运行测试代码

```
# mvn test
```

打包Maven项目

```
# mvn package
```

删除编译产生的东西

```
# mvn clean
```

安装jar到本地仓库

```
# mvn install
```





### Maven插件：打包源代码

到[https://maven.apache.org/plugins/index.html]( https://maven.apache.org/plugins/index.html )找到需要的插件

配置项目下的pom.xml文件，在`<project></project>`下添加

```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugin</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>3.2.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>jar-no-fork</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>

    </build>
```

`<plugins></plugins>`里面添加插件

`<executions></executions>`里面添加了当package时运行`jar-no-fork`打包源代码



### pom.xml常用元素

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.gzeis</groupId>
    <artifactId>testing</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>Testing project</name>
    <url>http://localhost</url>
    <description>fucking maven</description>
    <developers>
        <developer>
            <name>developer1</name>
            <email>xxxx@gmail.com</email>
        </developer>
    </developers>
    <licenses>
        <license>
            <name>MIT</name>
        </license>
    </licenses>
    <organization>
        <name>persional</name>
    </organization>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.10</version>
            <scope>test</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugin</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>3.2.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>jar-no-fork</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>

    </build>

</project>
```

`<project></project>`是pom.xml的根元素，包括了pom.xml的约束信息

`<modelVersion></modelVersion>`指定了当前pom.xml的版本

之后就是坐标信息

由`<groupId></groupId>` `<artifactId></artifactId>` `<version></version>` `<packaging></packaging>` 定义了Maven属于的实际项目

> `<groupId></groupId>公司地址反写+项目名</groupId>`标识了公司名和项目名
> `<artifactId></artifactId>项目名+模块名</artifactId>`标识了项目的模块
> `<version></version>`标识了当前的版本号，它由三个点分的数字表示，第一个数字表示大版本号，第二个数字表示分支版本号，第三个数字表示小版本号，小版本号也可以写成
>
> * SNAPSHOT 快照
> * ALPHA 内部测试
> * BETA 公测
> * RELEASE 稳定
> * GA 正式发布
>
> `<packaging></packaging>`代表Maven默认的打包方式，默认是jar

`<name></name>`表示项目描述名

`<url></url>`表示项目地址

`<description></description>`表示项目描述

`<developers><developers>`是开发人员列表

`<licenses></licenses>`是许可信息

`<organization></organization>`是组织信息



`<properties></properties>`里面定义的标签后面可以通过`${...}`来引用，比如：

```xml
<properties>
	<junit.version>4.10</junit.version>
</properties>

<dependencies>
	<dependency>
    	<groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>

</dependencies>
```



之后使用`<dependencies></dependencies>`来描述依赖列表，在里面使用`<dependency></dependency>`来指定每一个依赖，通常每个`<dependency></dependency>`包含`<groupId></groupId>`  `<artifactId></artifactId>`  `<version></version>` 就是写一个Maven项目是最开始写的那些

还有`<type></type>` `<scope></scope>`表示依赖的范围，比如写了`test`就表示这个依赖只在test下有用，在main下会报错，`system` `provided`在编译测试时有效，后者会和本机系统关联，可移植性差，`import`是导入范围

`<optional></optional>`表示依赖是否可选，默认是false，如果是false子项目默认是继承的，如果是true子项目必须显示引入依赖

`<exclusions><exclusion></exclusion></exclusions>`用来取消传递依赖，比如A依赖B，B依赖C，C对A来说就是传递依赖，如果A不想依赖C就在A中用这个标签exclude掉C的坐标



`<dependencyManagement></dependencyManagement>`是依赖管理，里面的`<dependency></dependency>`，依赖的包并不会在本项目执行



`<build></build>`用来为构建行为提供支持

通常里面使用`<plugins></plugins>`定义插件列表，`<plugin></plugin>`定义插件，比如上面的source，也是有`<groupId></groupId>` `<artifactId></artifactId>` `<version></version>`来定义



`<parent></parent>`通常用于在子模块中对父模块pom.xml的继承

`<modules></modules>`用来聚合多个运行的Maven项目，里面使用`<module></module>`

聚合项目

父项目配置modules：

```xml
<modules>
	<module>../artifactId-001</module>
	<module>../artifactId-002</module>
	<module>../articleId-003</module>
</modules>
```

子项目配置父项目：

```xml
<parent>
    <artifactId>parentartifact</artifactId>
    <groupId>com.example</groupId>
    <version>1.0-SNAPSHOT</version>
</parent>
<modelVersion>4.0.0</modelVersion>
```



#### 建立模块项目

1. 创建一个空的maven项目，pom.xml正常配置

2. 在pom.xml中添加子项目的modules

   `<module>`标签为子项目的路径

   ```xml
   <modules>
       <module>springlearn01</module>
       <module>HelloSpring02</module>
       <module>springioc03</module>
       <module>springdi04</module>
       <module>springautowired05</module>
       <module>springanno06</module>
   </modules>
   ```

3. 子项目添加`<parent>`标签

   子项目只需要添加artifact id即可

   ```xml
   <parent>
       <artifactId>springlearn</artifactId>
       <groupId>org.example</groupId>
       <version>1.0-SNAPSHOT</version>
   </parent>
   <artifactId>springanno06</artifactId>
   ```

   



## Errors

### IDEA运行Maven程序

如果运行提示

```
Error: java error: release version 5 not supported
```

1. 进入File -> Project Structure或者`Ctrl + Alt + Shift + S`
2. 设置Project SDK和Project language level的JDK版本一样
3. 设置Modules的Sources下的Language level也要和之前一样
4. 进入File -> Settings或者`Ctrl + Alt + S`
5. 进入Build, Execution, Deployment -> Compiler -> Java Compiler
6. 把Per-module bytecode version里面的项目（当前项目）改成和上面一样的JDK版本，或者改Project bytecode version那里改成和上面一样的JDK版本（以后创建的项目就是按这个JDK）
7. 搞定

参考链接：[https://blog.csdn.net/qq_22076345/article/details/82392236](https://blog.csdn.net/qq_22076345/article/details/82392236)



### 命令行运行Maven程序

如果运行提示

```
Error: java error: release version 5 not supported
```

在`<project></project>`里面加上下面这段配置，让他使用JDK1.8

```xml
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
        <java.version>1.8</java.version>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
</properties> 
```





### IDEA无法识别SpringBoot程序

#### 方法1

打开设置（Ctrl+Alt+S），在keymap搜索` Invalidate Caches/Restart `并设置快捷键，然后清除缓存并重启IDEA



#### 方法2

检查重新导入pom依赖，在pom文件（ 右键 --> maven -->reimport ）



### Maven无法打包java目录下的资源文件

在pom.xml中添加下面的配置，打包proterties，yml，xml

```xml
<build>
    <resources>
      <resource>
        <directory>src/main/java</directory>
        <includes>
          <include>**/*.properties</include>
          <include>**/*.yml</include>
          <include>**/*.xml</include>
        </includes>
        <filtering>true</filtering>
      </resource>

      <resource>
        <directory>src/main/resources</directory>
        <includes>
          <include>**/*.properties</include>
          <include>**/*.yml</include>
          <include>**/*.xml</include>
        </includes>
        <filtering>true</filtering>
      </resource>
    </resources>
  </build>
```



### Maven编译显示`Caused by: org.apache.maven.plugin.compiler.CompilationFailureException: Compilation failure`

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



### 编译异常`An API incompatibility was encountered while executing org.apache.maven.plugins:maven-jar-plugin:2.6:jar`

```
ERROR] Failed to execute goal org.apache.maven.plugins:maven-jar-plugin:2.6:jar (default-jar) on project my-test-utils: Execution default-jar of goal org.apache.maven.plugins:maven-jar-plugin:2.6:jar failed: An API incompatibility was encountered while executing org.apache.maven.plugins:maven-jar-plugin:2.6:jar: java.lang.ExceptionInInitializerError: null
[ERROR] -----------------------------------------------------
[ERROR] realm =    plugin>org.apache.maven.plugins:maven-jar-plugin:2.6
[ERROR] strategy = org.codehaus.plexus.classworlds.strategy.SelfFirstStrategy
[ERROR] urls[0] = file:/Users/jeanvaljean/.m2/repository/org/apache/maven/plugins/maven-jar-plugin/2.6/maven-jar-plugin-2.6.jar
[ERROR] urls[1] = file:/Users/jeanvaljean/.m2/repository/org/slf4j/slf4j-jdk14/1.5.6/slf4j-jdk14-1.5.6.jar
[ERROR] urls[2] = file:/Users/jeanvaljean/.m2/repository/org/slf4j/jcl-over-slf4j/1.5.6/jcl-over-slf4j-1.5.6.jar
[ERROR] urls[3] = file:/Users/jeanvaljean/.m2/repository/org/apache/maven/reporting/maven-reporting-api/2.2.1/maven-reporting-api-2.2.1.jar
[ERROR] urls[4] = file:/Users/jeanvaljean/.m2/repository/org/apache/maven/doxia/doxia-sink-api/1.1/doxia-sink-api-1.1.jar
[ERROR] urls[5] = file:/Users/jeanvaljean/.m2/repository/org/apache/maven/doxia/doxia-logging-api/1.1/doxia-logging-api-1.1.jar
[ERROR] urls[6] = file:/Users/jeanvaljean/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar
[ERROR] urls[7] = file:/Users/jeanvaljean/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar
[ERROR] urls[8] = file:/Users/jeanvaljean/.m2/repository/org/codehaus/plexus/plexus-interactivity-api/1.0-alpha-4/plexus-interactivity-api-1.0-alpha-4.jar
[ERROR] urls[9] = file:/Users/jeanvaljean/.m2/repository/backport-util-concurrent/backport-util-concurrent/3.1/backport-util-concurrent-3.1.jar
[ERROR] urls[10] = file:/Users/jeanvaljean/.m2/repository/org/sonatype/plexus/plexus-sec-dispatcher/1.3/plexus-sec-dispatcher-1.3.jar
[ERROR] urls[11] = file:/Users/jeanvaljean/.m2/repository/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar
[ERROR] urls[12] = file:/Users/jeanvaljean/.m2/repository/org/codehaus/plexus/plexus-interpolation/1.11/plexus-interpolation-1.11.jar
[ERROR] urls[13] = file:/Users/jeanvaljean/.m2/repository/org/apache/maven/maven-archiver/2.6/maven-archiver-2.6.jar
[ERROR] urls[14] = file:/Users/jeanvaljean/.m2/repository/org/apache/maven/shared/maven-shared-utils/0.7/maven-shared-utils-0.7.jar
[ERROR] urls[15] = file:/Users/jeanvaljean/.m2/repository/com/google/code/findbugs/jsr305/2.0.1/jsr305-2.0.1.jar
[ERROR] urls[16] = file:/Users/jeanvaljean/.m2/repository/org/codehaus/plexus/plexus-utils/3.0.20/plexus-utils-3.0.20.jar
[ERROR] urls[17] = file:/Users/jeanvaljean/.m2/repository/org/codehaus/plexus/plexus-archiver/2.9/plexus-archiver-2.9.jar
[ERROR] urls[18] = file:/Users/jeanvaljean/.m2/repository/org/codehaus/plexus/plexus-io/2.4/plexus-io-2.4.jar
[ERROR] urls[19] = file:/Users/jeanvaljean/.m2/repository/commons-io/commons-io/2.2/commons-io-2.2.jar
[ERROR] urls[20] = file:/Users/jeanvaljean/.m2/repository/org/apache/commons/commons-compress/1.9/commons-compress-1.9.jar
[ERROR] Number of foreign imports: 1
[ERROR] import: Entry[import  from realm ClassRealm[maven.api, parent: null]]
[ERROR] 
```

添加以下插件

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.9</source>
                <target>1.9</target>
                <jdkToolchain>
                    <version>9</version>
                </jdkToolchain>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.0.2</version>
        </plugin>
    </plugins>
</build>
```

[StackOverFlow]( https://stackoverflow.com/questions/46353477/jdk9-and-maven-jar-plugin?r=SearchResults )

