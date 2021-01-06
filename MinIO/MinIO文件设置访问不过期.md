# 设置MinIO的分享链接不过期

1. 添加远程主机

   ```shell
   $ mc config host add <别名> <远程主机:端口> <用户名> <密码>
   ```

2. 通过 `mc` 命令设置一个bucket为公共的

   ```shell
   $ mc policy set public <远程主机别名>/<需要设置的bucket>
   ```

   这个操作要通过 `mc` 命令设置

   此时访问 `http://主机地址:端口/公开访问的bucket/文件` 就能访问

3. 通过 `minioClient.getPresignedObjectUrl()` 返回的URL中带的请求参数有超时限制，我们需要去掉查询参数，即 `?` 之后的内容
   ```java
   return minioClient.getPresignedObjectUrl(Method.GET,
                   minioProperties.getBucket(),
                   storageName,
                   1, null).split("\\?")[0];
   ```


