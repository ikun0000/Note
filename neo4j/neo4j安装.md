# Neo4j安装



## docker安装

1. 下载neo4j的镜像

   ```shell
   $ docker pull neo4j
   ```

   neo4j有两个版本，分别是社区版和企业版，如果想要下载企业版就带上有 `enterprise` 的标签的版本

2. 启动neo4j

   ```shell
   $ docker run -dit -p 7474:7474 -p 7687:7687 -v $HOME/neo4j/data:/data neo4j
   ```

   此时可以访问**http://neo4jhost:7474**的web界面控制台了

   默认情况是需要登陆的，用户名密码分别是 `neo4j/neo4j` ，登陆进去后会要求重置密码。

   如果这是开发环境使用的不想有登陆验证可以在启动容器时加上 `--env=NEO4J_AUTH=none` 关闭认证

   