# Go工具

- [ ] **gocode **自动补全，不适用于模块
- [ ] **gopkgs** 自动完成未导入的软件包并添加导入功能
- [ ] **go-outline** 转到文件中的符号
- [ ] **go-symbols** 转到工作区中的符号
- [ ] **guru ** 找到所有参考并转到符号的实现
- [ ] **gorename ** 重命名符号
- [ ] **gotests** 生成单元测试
- [ ] **gomodifytags** 修改结构上的标签
- [ ] **impl** 接口的存根
- [ ] **fillstruct** 用默认值填充结构
- [ ] **goplay** The Go playground
- [ ] **godoctor** 提取到函数和变量
- [ ] **dlv** 调试
- [ ] **gocode-gomod** 自动完成，与模块一起使用
- [ ] **godef** Go定义
- [ ] **goreturns** 格式化程序
- [ ] **golint** 短绒



| 工具         | 下载地址(go get -u -v)                         |
| ------------ | ---------------------------------------------- |
| gocode       | github.com/mdempsky/gocode                     |
| gopkgs       | github.com/uudashr/gopkgs/v2/cmd/gopkgs        |
| go-outline   | github.com/ramya-rao-a/go-outline              |
| go-symbols   | github.com/acroca/go-symbols                   |
| guru         | golang.org/x/tools/cmd/guru                    |
| gorename     | golang.org/x/tools/cmd/gorename                |
| gotests      | github.com/cweill/gotests/...                  |
| gomodifytags | github.com/fatih/gomodifytags                  |
| impl         | github.com/josharian/impl                      |
| fillstruct   | github.com/davidrjenni/reftools/cmd/fillstruct |
| goplay       | github.com/haya14busa/goplay/cmd/goplay        |
| godoctor     | github.com/godoctor/godoctor                   |
| dlv          | github.com/go-delve/delve/cmd/dlv              |
| gocode-gomod | github.com/stamblerre/gocode                   |
| godef        | github.com/rogpeppe/godef                      |
| goreturns    | github.com/sqs/goreturns                       |
| golint       | golang.org/x/lint/golint                       |

如果`go get <package address>`安装不了直接去github下载下来放在`$GOPATH/src`下，然后执行`go install <package address>`就可以了

golang.org/x/tools/cmd/guru 这种无法访问的网站去github的golang下面下载，比如这个对应的在github的项目就是  https://github.com/golang/tools  下的cmd/guru 把这个项目 pull到 `$GOPATH/src`中对应的 golang.org/x 下再执行`go install <package address>`即可，他会先从本地目录查找有没有对应的如果没有才去远程下载，如果中途还是提示缺少什么库也是去github下下来然后先安装好依赖的包



# Go Proxy

关于goproxy，简单来说就是一个代理，让我们更方便的下载哪些由于墙的原因而导致无法下载的第三方包，比如golang.org/x/下的包，虽然也有各种方法解决，但是，如果是你在拉取第三方包的时候，而这个包又依赖于golang.org/x/下的包，你本地又恰恰没有，当然不嫌麻烦的话，也可以先拉取golang.org/x/下的包，再拉取第三方包。

```shell
$ set GO111MODULE=on

$ set GOPROXY=https://mirrors.aliyun.com/goproxy
# 或者
$ set GOPROXY=https://goproxy.io

$ go get github.com/kataras/iris/v12@latest
```

[Goproxy 中国]( https://goproxy.cn/ )

