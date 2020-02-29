# 解决Tomcat日志中文乱码

1. 打开Tomcat安装目录下的conf/logging.properties文件

2. 修改配置

   ```
   java.util.logging.ConsoleHandler.encoding = UTF-8
   ```

   改成

   ```
   java.util.logging.ConsoleHandler.encoding = GBK
   ```

   