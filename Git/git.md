# git常用命令



## 参考

> [廖学峰官方网站](https://www.liaoxuefeng.com/wiki/896043488029600)
>
> [工作中 99% 能用到的 Git 命令](http://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247487283&idx=2&sn=0e726185ef62f04f3510b19f77e360ef&chksm=9bed2e41ac9aa757923892cffff3de6cf81b9c266227cf8a560c9c40371560674688ec8100dc&scene=0&xtrack=1#rd)



#### 初始化配置

```
# git config --global user.name "Your Name"
# git config --global user.email "email@example.com"
# git config --global color.ui true
# git config --list       // 列出所有配置信息
```

* 其中Youname和email@example.com换成要用的用户名和密码，git每一次记录修改的时候都要使用这两个东西

* 最后一条配置git显示颜色

* 配置完之后会在用户家目录出现一个.gitconfig的文件，就是上面配置的内容



#### 创建版本库

```
# mkdir learngit
# cd learngit
# git init
// 此时会在当前目录生成一个.git的目录，这个目录记录了每一次修改
```

将文件及修改写入版本库：

```
# git add readme.txt
# git commit -m "wrote a readme file"
# git commit --amend 
或者
# git commit --amend -m "说明"
```

* 第一个命令是将文件放入stage（还没上传到版本库），第二条命令就是将stage的所有内容写入到版本库，并且使用-m带上说明
* 第三条命令可以更正提交说明



#### 时光机穿梭

```
# git status
// 这个命令用来查看当前的状态（有没有文件修改了没有写入到cache，有没有文件没有写入版本库）
```

```
# git checkout -- <file>
// 使用版本库的文件还原当前目录的文件，也可以使用:
# git diff readme.txt
# git diff 快照ID 快照ID
# git diff 快照ID
# git diff --cached
// 第二个命令比较两个快照
// 第三个命令比较当前工作目录和指定快照的区别
// 第四个命令比较暂存区域和仓库的区别
```

```
# git log
# git log --pretty=oneline      // 用来把所有显示显示为一行
/* 
查看版本记录，会有上传用户及其email的记录，还有上传时间，同时会生成一个commit id（用来做时间回退，有点像 vmware的快照）注意：这里显示的只是当前版本到之前版本的记录，如果之前回到某个版本就不能看到后面的记录
* /
```

```
# git reset --hard HEAD^
# git reset --head HEAD~          // 和^差不多
# git reset --head HEAD~10      // 相当于10个波浪线
# git reset --soft HEAD~           // 移动HEAD的指向
# git reset --mixed HEAD~   // 将快照回滚到暂存区域，默认
# git reset --hard HEAD~     //将暂存区域还原到工作目录
# git reset --hard <commit id>
# git reflog
/*
第一条命令用来回退到上一个版本，其中HEAD指向当前版本，HEAD^就是上一个版本，也可以用HEAD^^回退到上上个版本。第二条命令就是使用commit id回退。最后那条就是用来查看你处于的版本记录（会显示commit id）
*/

# git revert HEAD~3
/*
还原第四次前的提交，并在还原更改后创建一次新的提交
*/
```

```
/*
如果你发现你在工作区的文件写了些像stupid boss这些东西的话就要用下面这个命令撤销掉这个文件的修改（不怕死可以不改）
*/

# git checkout -- <file>

/*
如果很不幸你提交到stage的话，可以用下面这个命令把stage的文件撤销掉
*/

# git reset HEAD <file>

/*
如果要删除文件，使用下面这个命令删除文件
*/

# git rm readme.txt      // -f参数强制删除

// 修改文件名和移动文件，加上路径就是移动文件
# git mv readme.txt readme.md     // Linux用mv，windows用ren
```



#### git提交结构（workspace，stage，master）：

![](img/0.jpg)

![执行add命令](img/1.jpg "执行add命令")

![执行commit命令](img/2.jpg "执行commit命令")



#### 远程仓库

```
/* 
首先要在本地生成ssh key，并且加到你的github的ssh设置那里，不然你的电脑连不上。
然后再github上建立一个仓库，然后就会出现一些命令，就是用来给你克隆这个残酷到本地的
*/

# git remote add origin git.michaelliao/learngit.git

// 推送本地到远程仓库
# git push -u origin master

// 使用远程仓库更新本地，备份一条分支
# git pull origin master

// 推送本地到远端origin
# git push origin

// 查看项目和那些远端仓库相连
# git remote
# git remote -v

// 拉取远端仓库，fetch是拉取全部的东西，pull只是拉取一个分支
# git fetch

// git clone的做法
$ mkdir project.git
$ cd project.git
$ git init
$ git remote add -f -t master -m master来源git：//example.com/git.git/
$ git merge origin
```



#### 分支管理

```
// 创建分支：
# git branch dev
# git checkout -b dev     // 创建dev分支并移动到dev分支

checkout加上-b参数就是创建加移动到这个分支，相当于：
# git branch dev
# git checkout dev

/*
如果用git checkout -b 不加上分支名会创建匿名分支，匿名分支所做的任何提交无效（可以用来做实验）
*/

// 查看当前分支
# git branch


// 合并分支：
# git checkout master
# git merge dev		// 合并dev分支到master分支

// 删除分支：
# git branch -d dev

// 使用git log查看分支合并情况
# git log --graph --pretty=oneline --abbrev-commit
// --graph 用来显示分支图
// --pretty=oneline显示一行
```



#### 标签管理

```
// 打标签：
# git tag v1.0
// 在当前分支当前版本打一个v1.0的标签

// 查看标签：
# git tag

// 给一个提交打标签：
# git tag v0.9 f52c633     // 给f52c633的提交打一个v0.9的标签

// 查看标签信息
# git show <tag name>

// 制定标签的说明信息：
# git tag -a <tagname> -m "blablabla…"

// 删除标签：
# git tag -d v0.1

// 推送标签到远程仓库：
# git push origin v1.0
# git push origin v1.0         //一次性推送全部没有推送的标签

// 删除远程标签：
# git tag -d v0.9       //先删除本地
# git push origin :refs/tags/v0.9           //在删除远程的

```

