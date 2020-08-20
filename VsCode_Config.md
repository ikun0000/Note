# VSCode配置



## 插件

* Beautify：格式化前端代码
* Bootstrap 4, Font awesome 4, Font Awesome 5 Free & Pro snippets：Bootstrap4支持
* C/C++：C/C++支持
* Code Runner：自动运行代码
* Git Graph：图形化展示Git分支
* Git History：显示Git提交历史
* GitLens — Git supercharged：Git支持
* Go：Go开发支持
* HTML CSS Support：CSS支持
* HTML Snippets：HTML支持
* JavaScript (ES6) code snippets：JS语法支持
* Live Server：开发前端程序的服务器
* markdownlint：Markdown支持
* Nodejs Snippets：开发node支持
* One Dark Pro：一个主题
* Python：Python开发支持
* Sass/Less/Scss/Typescript/Javascript/Jade/Pug Compile Hero：前端模板支持
* TranslationToolbox：翻译插件
* Vetur：Vue开发支持
* vscode-icons：VsCode图标风格
* Vue VSCode Snippets：VsCode支持Vue语法
* Webpack Snippets：Webpack包管理支持




## setting.json

```json
{
    "window.zoomLevel": 0,
    "terminal.integrated.fontSize": 20,
    "editor.multiCursorModifier": "ctrlCmd",
    "editor.formatOnPaste": true,
    "editor.fontSize": 18,
    "editor.detectIndentation": false,
    "editor.fontFamily": "Consolas, 'Courier New', monospace",
    "debug.console.fontSize": 18,
    "files.autoSave": "afterDelay",
    "files.autoSaveDelay": 1000,
    "workbench.colorTheme": "One Dark Pro",
    "workbench.iconTheme": "vscode-icons",

    "go.autocompleteUnimportedPackages": true,
    "go.gopath": "D:\\gopath\\go",
    "go.goroot": "D:\\Go",
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
    },

    "C_Cpp.autoAddFileAssociations": true,
    "C_Cpp.autocomplete": "Default",
    "C_Cpp.codeFolding": "Enabled",

    "javascript.suggest.includeAutomaticOptionalChainCompletions": true,
    "javascript.format.enable": true,
    "gitlens.advanced.messages": {
        "suppressGitVersionWarning": true
    },

    "code-runner.runInTerminal": true,
    "code-runner.ignoreSelection": true,
    "code-runner.fileDirectoryAsCwd": true,
    
}
```

