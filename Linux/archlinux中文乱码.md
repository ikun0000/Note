# Arch Linux中文乱码解决方案



## 安装中文字体   

乱码的原因就是缺少中文字体的支持，下载文泉驿xx， OK了

```shell
$ npacman -S wqy-zenhei ttf-fireflysung
```

  /etc/locale.gen 设置en_US.UTF8 UTF-8 zh_CN.UTF8 UTF-8

 

## 执行下面命令看看是否配置好了中文编码：

```shell
$ locale-gen 
```

```shell
$ locale
```

```shell
$ locale -a
```



## /etc/rc.conf 中
```
 LOCALE=en_US.UTF-8
```
