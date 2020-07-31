# 解决VsCode Go语言自动补全慢



如果之前安装了golang.org的tools包的话就直接在vscode的setting.json文件中添加一行配置

```json
go.useLanguageServe: true
```

然后重启，随便打开一个go文件就会在右下角提示安装**gopls**，第健壮即可，如果安装不上基本就是被墙了，就先下载 `https://github.com/golang/tools` 到**${GOPATH}/src/golang.org/x/**下再执行安装**gopls**就可以了

vscode配置

**setting.json**

```json
"go.useLanguageServer": true,
"[go]": {
    "editor.formatOnSave": true,
    "editor.codeActionsOnSave": {
        "source.organizeImports": true,
    },
    // Optional: Disable snippets, as they conflict with completion ranking.
    "editor.snippetSuggestions": "none",
},
"[go.mod]": {
    "editor.formatOnSave": true,
    "editor.codeActionsOnSave": {
        "source.organizeImports": true,
    },
},
"gopls": {
     // Add parameter placeholders when completing a function.
    "usePlaceholders": true,

    // If true, enable additional analyses with staticcheck.
    // Warning: This will significantly increase memory usage.
    "staticcheck": false,
}
```

[其他编辑器配置点这里查看]( https://github.com/golang/tools/tree/master/gopls/doc )