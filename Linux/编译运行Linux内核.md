# 基于QEMU编译运行Linux内核



## 环境

最好使用root



### 操作系统

linuxmint-20.2-xfce-64bit

预备工作：

```shell
$ sudo apt install build-essential
$ sudo apt install gcc-multilib
```



### 文件系统

busybox-1.33.1

预备工作：

```shell
$ sudo apt-get install libncurses5-dev
```

进入busybox的目录

```shell
$ make menuconfig
```

进入Settings -> Build Options 选中Build static binary (no shared libs)

```
# make menuconfig

Busybox Settings  --->
      Build Options  --->
            [*] Build BusyBox as a static binary (no shared libs)
```



然后

```shell
$ make 
$ make install
```



### 内核版本

linux-5.14.3

```shell
$ tar -Jxvf linux-5.14.3.tar.xz
$ sudo apt-get install libssl-dev
$ sudo apt-get install libelf-dev
$ export ARCH=x86
$ make x86_64_defconfig
$ make menuconfig
```

General setup 设置Initial RAM filesystem and RAM disk (initramfs/initrd) support

 Device Drivers -> Block devices设置RAM block device support ，Default RAM disk size (kbytes) 为65536

```
General setup  --->

       ----> [*] Initial RAM filesystem and RAM disk (initramfs/initrd) support

    Device Drivers  --->

       [*] Block devices  --->

               <*>   RAM block device support

               (65536) Default RAM disk size (kbytes)
```



```shell
$ make
```

目标文件在 arch/x86_64/boot/bzImage



### QEMU版本

qemu-6.1.0

```shell
$ sudo apt-get install ninja-build
$ sudo apt-get install libglib2.0-dev
$ sudo apt-get install libpixman-1-dev
```

下载编译QEMU

```shell
$ wget https://download.qemu.org/qemu-6.1.0.tar.xz
$ tar -Jxvf qemu-6.1.0.tar.xz
$ cd qemu-6.1.0
$ ./configure
$ make
```



## 运行

配置最小文件系统，到busybox的_install目录下

```shell
# mkdir etc dev mnt
# mkdir -p proc sys tmp mnt
# mkdir -p etc/init.d/
# vim etc/fstab
proc        /proc           proc         defaults        0        0
tmpfs       /tmp            tmpfs    　　defaults        0        0
sysfs       /sys            sysfs        defaults        0        0
# vim etc/init.d/rcS
echo -e "Welcome to linux kernel"
/bin/mount -a
echo -e "Remounting the root filesystem"
mount  -o  remount,rw  /
mkdir -p /dev/pts
mount -t devpts devpts /dev/pts
echo /sbin/mdev > /proc/sys/kernel/hotplug
mdev -s
# chmod 755 etc/init.d/rcS
# vim etc/inittab
::sysinit:/etc/init.d/rcS
::respawn:-/bin/sh
::askfirst:-/bin/sh
::ctrlaltdel:/bin/umount -a -r
# chmod 755 etc/inittab
# cd dev
# mknod console c 5 1
# mknod null c 1 3
# mknod tty1 c 4 1
```

打包文件系统成镜像，在busybox的目录下写个脚本执行，或者手动执行：

```shell
#!/bin/bash
rm -rf rootfs.ext4
rm -rf fs
dd if=/dev/zero of=./rootfs.ext4 bs=1M count=32
mkfs.ext4 rootfs.ext3
mkdir fs
mount -o loop rootfs.ext4 ./fs
cp -rf ./_install/* ./fs
umount ./fs
gzip --best -c rootfs.ext4 > rootfs.img.gz
```

执行：

```shell
$ qemu-system-x86_64 -kernel ./linux-5.14.3/arch/x86_64/boot/bzImage -initrd ./busybox-1.33.1/rootfs.img.gz -append "root=/dev/ram init=/linuxrc" -serial file:output.txt
```

