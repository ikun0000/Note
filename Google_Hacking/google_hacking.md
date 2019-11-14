# Google Hacking

#### 通配符

* -keyword

  功能：强制结果不要出现此关键字

  举例：电影 -黑客

* *keyword

  功能：模糊搜索，强制结果包含此关键字

  举例：电影 一个叫*决定*

* "keyword"

  功能：强制搜索结果出现此关键字

  举例：书籍"web安全"



#### site

搜索指定域名的网页内容

可以用于搜索子域名，跟此域名有关的内容

```
site:zhihu.com
// 搜索跟知乎有关的网页

"web安全" site:zhihu.com
// 搜索zhihu.com跟web安全有关的网页

"sql注入" site:csdn.net
// 搜索csdn.net跟sql注入有关的网页

"教程" site:pan.baidu.com
// 在百度盘中搜索教程

".pdf" site:pan.baidu.com
// 搜索百度盘中的PDF书籍
```



#### filetype

搜索指定文件类型

```
"web安全" filetype:pdf
// 搜索跟web安全相关的书籍文件

nmap filetype:ppt
// 搜索跟nmap相关的ppt

site:csdn.net filetype:pdf
// 搜索csnd网站中的PDF文件
```



#### inurl

搜索url网址存在特定关键字的网页

可以用来搜寻有注入点的网站

 ```
inrul:.php?id=
// 搜索网址中有"php?id"的网页

inrul:view.php=?
// 搜索网址中有"view.php="的网页

inrul:.jsp?id=
// 搜索网址中有"jsp?id="的网页

inrul:.asp?id=
// 搜索网址中有"asp?id="的网页

inrul:/admin/login.php
// 搜索网址中有"/admin/login.php"的网页

inrul:login
// 搜索网址中有"login"的网页
 ```



##### intitle

搜索标题存在特定关键字的网页

```
inrul:后台登陆
// 搜索网页标题中有“后台登陆”的页面

intitle:后台管理 filetype:php
// 搜索网页标题中有“后台管理”的php页面

intitle:index of "keyword"
// 搜索此关键字相关的索引目录信息

intitle:index of "parent directory"
// 搜索根目录相关的索引目录信息

intitle:index of "password"
// 搜索密码相关的索引目录信息

intitle:index of "login"
// 搜索登陆页面信息

intitle:index of "admin"
// 搜索后台管理页面信息
```



##### intext

搜索正文存在特定关键字的页面

```
intext:Powered by Discuz
// 搜索Discuz论坛相关页面

intext:powered by wordpress
// 搜索wordpress制作的网址

intext:Powered by *CMS
// 搜索*CMS相关的页面

intext:Powered by xxx inrul:login
// 搜索此类网址的后台登陆页面
```

