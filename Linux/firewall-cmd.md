# firewall-cmd常用方法

```
# yum install firewalld firewall-config
// 安装firewalld

# systemctl start firewalld
// 开启firewalld
# systemctl status firewalld
// 查看firewalld状态
# systemctl disable firewalld
// 取消firewalld开机启动
# systemctl stop firewalld
// 关闭firewalld
# systemctl enable firewalld
// 开机启动firewalld

# systemctl stop firewalld
# systemctl disable firewalld
# yum install iptables-services
# systemctl start iptables
# systemctl enable iptables
// 安装和使用iptables

 # firewall-cmd --get-active-zone
// 查看区域信息

# firewall-cmd --get-zone-of-interface=ens33
// 查看指定网口的区域

# firewall-cmd --panic-on
// 拒绝所有包

# firewall-cmd --panic-off
// 取消拒绝状态

# firewall-cmd --query-panic
// 查询是否拒绝

# firewall-cmd --reload
// 更新防火墙规则

# firewal-cmd --zone=putlic --add-interface=eth0
// 将接口加入到区域中，默认是public

# firewall-cmd --set-default-zone=home
// 设置默认区域，无需重启立即生效

# firewall-cmd --get-default-zone
// 获取当前的默认区域

# firewall-cmd --zone=dmz --list-ports
// 查看指定区域打开的端口

# firewall-cmd --zone=work --add-service=smtp
// 打开一个服务，类似于端口可视化，服务需要在配置目录中添加，/etc/firewalld目录下的services文件

# firewall-cmd --zone=work --remove-service=smtp
// 移除一个服务

# firewall-cmd --get-zones
// 显示支持的区域列表

# firewall-cmd --zone=public --list-all
// 显示所有区域信息

# firewall-cmd --zone=public --change-interface=eth1
// 临时修改eth1为区域public

# firewall-cmd --get-services
// 列出支持的所有服务

# firewall-cmd --zone=public --list-services
// 列出指定区域的服务

 # firewall-cmd --zone=public --add-service=dns
// 添加dns服务

# firewall-cmd --zone=public --remove-service=dns
// 移除dns服务

# firewall-cmd --zone=public --add-port=3306/tcp --permanent
// 永久打开3306/tcp端口
// --permanent：永久性

# firewall-cmd --zone=public --remove-port=3306/tcp
// 移除一个端口

# firewall-cmd --query-masquerade
// 检查是否允许伪造IP

# firewall-cmd --add-masquerade
// 允许firewalld伪造IP

# firewall-cmd --remove-masquerade
// 禁止防火墙伪造IP

# firewall-cmd --add-forward-port=port=80:proto=tcp:toport=8080
// 将80端口的数据包重定向到8080端口，要开启IP伪造

# firewall-cmd --add-forward-port=port=22:proto=tcp:toaddr=192.168.1.1:toport=2222
// 将22号端口的流量重定向到192.168.1.1的2222端口

# firewall-cmd --remove-forward-port=port=22:proto=tcp:toaddr=192.168.1.1:toport=2222
// 移除上面添加的forward规则

# firewall-cmd --zone=public --add-rich-rule="rule family="ipv4" port protocol="tcp" port="69" drop"
# fireall-cmd --zone=public --add-rich-rule="rule family="ipv4" port protocol="udp" port="53" accept"
# firewall-cmd --zone=public --add-rich-rule="rule protocol value="icmp" drop"
// rule语法 rule family="[protocol name]" port protocol="[protocol]" port="[port number]" [accept/drop]
// 过滤端口规则
// rule语法 rule protocol value="[icmp]" [accept/drop]
// 过滤协议规则

```

