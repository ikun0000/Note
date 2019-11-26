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

按住ctrl+w之后按方向键或者`h` `i` `j` `k`在不同屏幕间移动



在command模式下使用`:vs [filename]`(vertical split)进行竖直（左右）分屏，使用`:sp [filename]` (split)进行水平（上下）分屏

使用`:%s/foo/bar/g`进行全局替换，`:n,ms/foo/bar/g`在n到m行进行替换，%是匹配全部

`:!<command>`则可以在command模式下执行系统命令

`:set nu`显示行号



在normal模式下按v进入visual模式，使用V进行选行，使用ctrl+v进行方块选择，在这个模式下使用方向键或者`h` `i` `j` `k`进行批量选择操作，相当于其他编辑器安装shift+方向键