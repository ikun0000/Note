# VIM配置和常用命令

## 安装vimplus

项目地址：` https://github.com/chxuan/vimplus `

根据README.md文件安装配置即可，使用不同版本的vimplus的插件配置文件也可能不同，具体到时候看README.md文件

我的常用配置

```

```



安装完vimplus之后一般YouCompleteMe是用不了的，要先删掉它原来的然后自行安装，安装方法按照README.md做就可以了



## vim使用方法
### 基本操作

normal模式进入insert模式，`i`(insert)，使用这个在字符前面插入、`a`(append)，他会在字符后面插入、`a`是在当前字符的后面插入，`o`(open a line below)，`o`的话会在当前行的下一行插入

还有三个就是上面三个的大写，`I`(insert before line)，它会在当前行的最前面插入、`A`(append after line)，它会在当前行的最后插入、`O`(append a line above)，它会在当前行的上一行插入



`Esc`返回normal模式，normal模式下`:w`是保存修改，`:q`是退出命令，`:wq`是保存退出，`:q!`是不保存强行退出

按住`ctrl+w`之后按方向键或者`h` `i` `j` `k`在不同屏幕间移动



在command模式下使用`:vs [filename]`(vertical split)进行竖直（左右）分屏，使用`:sp [filename]` (split)进行水平（上下）分屏

使用`:%s/foo/bar/g`进行全局替换，`:n,ms/foo/bar/g`在n到m行进行替换，%是匹配全部

`:!<command>`则可以在command模式下执行系统命令

`:set nu`显示行号

`:syntax on`打开代码语法高亮



在normal模式下按v进入visual模式，使用V进行选行，使用`ctrl+v`进行方块选择，在这个模式下使用方向键或者`h` `i` `j` `k`进行批量选择操作，相当于其他编辑器安装`shift+方向键`

normal模式下按下`gi`快速回到上一次编辑的地方插入



在insert模式下，`ctrl+u`删除当前行，`ctrl+w`删除上一个单词，`ctrl+h`删除上一个字符

`ctrl+n`搜索当前文件可以匹配的内容

`ctrl+[`从insert切换到normal模式



### VIM快速移动

在normal模式下：`h`向右，`j`向下，`k`向上，`l`向下

`w`/`W`移动到下一个word/WORD的开头，`e`/`E`移动到下一个word/WORD的结尾 `b`/`B`，回到上一个单词开头，理解为backword（**word代表非空白字符分割的单词，WORD代表空白字符分割的单词**）

在行间搜索字符，`f{char}`跳转到指定字符，使用`;`和`,`移动到下一个和上一个，`t{char}`则是跳转到char的前一个字符，`F{char}`向前搜索

在行之间用`0`移动到当前行的第一个字符，使用`^`移动到当前行的第一个非空白字符。使用`$`移动到当前行的末尾，`g_`移动到行尾非空白字符

使用`(` `)`在句子间移动，使用`{` `}`在段落间移动（用的不多，**中文和英文也不同**）

使用`gg`在任意位置移动到文件开头，使用`G`在任意位置移动到文件结尾，使用`ctrl+o`回退上一步

`H`/`M`/`L`分别跳转到屏幕的开头(head)、中间(middle)、末尾(lower)

`ctrl+u`/`ctrl+d`上下翻半页，`ctrl+f`/`ctrl+b`上下翻一页，`zz`定位到文件中间位置

使用`ctrl+e`/`ctrl+y把文件向上/向下对齐屏幕`



### VIM增删改查

直接进入insert模式编辑



在normal模式下使用`dh`或者`x`删除一个字符，使用`dw`(delete word)删除一个单词，使用`dd`删除一行，h、w、d戴代表文本对象，也可以配合数字执行多次

使用`dt{char}`(delete to char)来删除直到指定字符char



在要修改的字符上面使用`r{char}`(replace)将字符替换成char，使用`R`会向后替换

在要修改的字符上面按下`s`(substitute)之后会删除当前字符并且进入insert模式，使用`S`会删除整行并进入insert模式

使用`c`(change)配合文本对象可以实现上述的功能，`cw`删除单词进入插入模式，`C`删除整行然后插入



在command模式下使用`:/{word}`或者`:?{word}`向后和向前搜索word，之后可以使用`n`移动到下一个，使用`N`移动到上一个，然后使用`:/{word} -r`或者`:?{word} -r`取消高亮。如果没有高亮，在command模式下输入`:set hls`显示搜索高亮



### VIM搜索替换

subsitute命令查找并替换文本，而且支持正则表达式

```
:[range]s[ubstitute]/{pattern}/{string}/{flags}
```

* range表示范围，10, 20表示匹配替换10-20行，%表示全部
* pattern是要替换的模式，string是替换后的文本
* flags表示替换的标志
  * g(global)表示全局范围内执行
  * c(confirm)表示确认，可以确认或者拒绝修改
  * n(number)报告匹配到的次数而不替换，可以用来查询匹配次数