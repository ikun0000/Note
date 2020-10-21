# Android Studio Gradle下载依赖缓慢

在创建好项目之后找到 `Gradle Scripts` 目录

然后打开 `build.gradle` 文件，初始化环境下内容如下：

```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.2"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

把所有 `repositories` 块的 `jcenter()` 注释掉

在 `buildscript` 中的 `repositories` 添加 `maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}` 和 `maven { url "https://jitpack.io" }`

在 `allprojects` 的 `repositories` 中添加 `maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}` 

修改后的文件如下：

```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}
        maven { url "https://jitpack.io" }
        google()
//        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.2"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}
        google()
//        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

然后重新加载

选择File -> Sync Project With Gradle Files