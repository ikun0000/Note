# java远程debug

#### 让java程序启动时可以被调试器链接
```java
$ java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=1044 Main
Listening for transport dt_socket at address: 1044
haha
```

之后再IDEA打开要调试的项目，然后下断点，在Add Configuration中添加Remote，设置和java命令行的address一样的端口再运行调试即可

> 要调试什么就要找它的源代码，调试maven就要找maven代码，调试maven compile就要去找compile plugin的代码



#### 使所有java程序启动可以被调试链接

设置JAVA_TOOL_OPTIONS环境变量

```
$ export JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=1044
```

