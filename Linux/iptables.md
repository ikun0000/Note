# iptables常用方法
## iptables规则功能
> filter表：
> 主要和主机自身有关，主要负责防火墙功能 过滤本机流入流出的数据包是默认使用的表
* input：负责过滤所有没目标地址是本机的数据包

* forward：负责转发流经本机但不进入本机的数据包
* output：负责处理源地址的数据包

> nat表：
> 负责网络地址转换
* snat：源地址转换
* dnat：目标地址转换
* pnat：目标端口转换

> mangle表：
> 将报文拆开并修改报文标志位，最后封装起来
> 五个检查点（内置链）
* REROUTING
* INPUT
* FORWORD
* UTPUT
* POSTROUTING



### iptables查询表和链：
```
# iptables -L
// 列出filter表的信息

# iptables -L -n --line-number
// 显示filter表信息
// -n：以数字格式显示端口和IP
// --line-number：显示行号

# iptables -t nat -L -v
// 显示NAT表的详细信息
// -t <tables name>：指定表
// -v：显示详细信息 -v -vvv -vvvv ..可以显示更详细的信息

```

###  管理表：

```
# iptables -t filter -F INPUT
// 清空filter表INPUT链的所有规则
// -F [chain]：清空指定连的规则

# iptables -t filter -P INPUT DROP
// 设置filter表INPUT链的默认策略为DROP（丢弃）
// -P <chain> <policy>：设置默认策略

# iptables -t filter -A INPUT -p icmp -j ACCEPT
// 允许所有ICMP包通过
// -A <chain>：添加一条规则
// -p：指定协议类型
// -j <target>：指定匹配后的动作

```

| 关键字     | 功能             |
| :--------: | :--------------: |
| DROP       | 丢弃             |
| REJECT     | 拒绝             |
| ACCEPT     | 接受             |
| RETURN     | 返回主链继续匹配 |
| REDIRECT   | 端口重定向       |
| MASQUERADE | 地址伪装         |
| DNAT       | 目标地址转换     |
| SNAT       | 源地址转换       |
| MARK       | 打标签           |

```
# iptables -I INPUT 2 -p tcp --dport 22 -j ACCEPT
// 放行22/tcp（ssh）端口
// -I <chain> [number]：指定插入的链和序号
// --dport [!]<port number | port range>：指定目标端口或端口范围！表示不匹配
// --sport [!]<port number | port range>：指定源端口或端口范围

# iptables -A INPUT -p tcp -d 10.10.10.1 --dport 445 -j DROP
// 丢弃所有目标IPv4地址为10.10.10.1的目标端口为445的数据包
// -d <ip address | net>：指定目标地址
// -s <ip address | net>：制定源地址

# iptables -R INPUT 1 -p icmp -j DROP
// 替换filter表INPUT链的第一条规则，丢弃所有ICMP包（防ping）
// -R <chain> <number>：替换规则

# iptables -D INPUT 1
// 删除INPUT链的第一条规则
// -D <chain> <number>：删除规则

# service iptables save
# /etc/init.d/iptables save
// 保存规则

# echo 1 > /proc/sys/net/ipv4/ip_forward
# iptables -A FORWARD -j ACCEPT
# iptables -t nat -A PREROUTING -p tcp -d 57.11.22.33 --dport 22 -j DNAT --to-destination 192.168.1.1
// 将所有ssh流量重定向到192.168.1.1

# echo 1 > /proc/sys/net/ipv4/ip_forward
# iptables -A FORWARD -j ACCEPT
# iptables -t nat -A PREROUTING -p tcp -d 57.11.22.33 --dport 80 -j DNAT 192.168.1.1:8080
//将http流量重定向到别的地方，目标端口是8080

# echo 1 > /proc/sys/net/ipv4/ip_forward
# iptables -A FORWARD -j ACCEPT
# iptables -t nat -A POSTROUTING -s 10.10.10.0/24 -j SNAT --to-source 123.1.1.1
// 将所有10.10.10.0/24的数据包的源地址改成123.1.1.1转发出去

```

