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

系统语言环境配置文件：/etc/locale.conf



# 中文输入法



## 安装软件之前安装`archlinuxcn`

编辑`sudo vim /etc/pacman.conf`

- 在pacman.conf文件的最后增加以下两段代码：

```
[archlinuxcn]
Server = https://mirrors.tuna.tsinghua.edu.cn/archlinuxcn/$arch
#Server地址：https://github.com/archlinuxcn/mirrorlist-repo
```

- 安装archlinuxcn-keyring包导入GPG key

之所以要安装archlinuxcn-keyring包导入GPG key是因为要安装某些软件的时候会提示gpg签名错误损坏等问题，这个是因为没有导入key的原因，所以\需要导入一下GPG KEY，在终端中运行以下命令即可：

```
sudo pacman -S archlinuxcn-keyring
```

- 更新一下源

在终端中运行

```
sudo pacman -Sy
```

至此，能在Arch Linux系统中使用清华源




## 中文输入法

打开终端，输入

```shell
$ sudo pacman -S fcitx fcitx-im
$ sudo pacman -S fcitx-configtool   	#图形配置工具
$ sudo pacman -S sunpinyin    		#输入法
# or
$ sudo pacman -S fcitx-googlepinyin
```

编辑`sudo vim ~/.xprofile`，添加

```shell
# 定义fcitx运行的环境变量
export GTK_IM_MODULE=fcitx
export QT_IM_MODULE=fcitx
export XMODIFIERS="@im=fcitx"

# 开机启动fcitx
/usr/bin/fcitx
```

重新登录后，搜索fcitx，打开fcitx配置选项，添加`pinyin`输入法，按`ctrl`+`Space`键切换输入法
