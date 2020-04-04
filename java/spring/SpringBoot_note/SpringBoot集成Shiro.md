# SpringBoot集成Shiro

> [知乎文章]( https://zhuanlan.zhihu.com/p/54176956 )



#### Shiro官方教程

```she
# git clone https://github.com/apache/shiro.git
# git checkout shiro-root-1.5.1 -b shiro-root-1.5.1
```



#### 导入shrio依赖

```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-starter</artifactId>
    <version>1.5.1</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-web-starter</artifactId>
    <version>1.5.1</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.5.1</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.5.1</version>
</dependency>
```



#### 编写Realm类，重写认证和授权方法

```java
public class LevelUserRealm extends AuthorizingRealm {
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }
}
```



#### 编写配置类

```java
@Configuration
public class ShiroSecurityConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/401").setViewName("error/401");
    }

    // ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //配置Shiro过滤器
        /**
         * 内置Shiro过滤器实现相关拦截功能
         *      常用的过滤器有：
         *          anon  : 无需认证（登录）可以访问
         *          authc : 必须认证才能访问
         *          user  : 如果使用rememberMe的功能可以直接访问
         *          perms : 该资源必须得到资源访问权限才可以使用
         *          role  : 该资源必须得到角色授权才可以使用
         */
        HashMap<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/", "anon");
        filterMap.put("/index", "anon");
        filterMap.put("/index.html", "anon");
        filterMap.put("/form", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/level1/*", "perms[level1]");
        filterMap.put("/level2/*", "perms[level2]");
        filterMap.put("/level3/*", "perms[level3]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);



        // 设置未授权跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/401");

        // 设置登陆请求
        shiroFilterFactoryBean.setLoginUrl("/form");

        return shiroFilterFactoryBean;
    }

    // DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("levelUserRealm") LevelUserRealm levelUserRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 关联Realm
        defaultWebSecurityManager.setRealm(levelUserRealm);
        return defaultWebSecurityManager;
    }

    // Realm
    @Bean
    public LevelUserRealm levelUserRealm() {
        return new LevelUserRealm();
    }

}
```



#### 添加Shiro内置过滤器

```java
//配置Shiro过滤器
/**
         * 内置Shiro过滤器实现相关拦截功能
         *      常用的过滤器有：
         *          anon  : 无需认证（登录）可以访问
         *          authc : 必须认证才能访问
         *          user  : 如果使用rememberMe的功能可以直接访问
         *          perms : 该资源必须得到资源访问权限才可以使用
         *          role  : 该资源必须得到角色授权才可以使用
         */
HashMap<String, String> filterMap = new LinkedHashMap<>();
filterMap.put("/", "anon");
filterMap.put("/index", "anon");
filterMap.put("/index.html", "anon");
filterMap.put("/form", "anon");
filterMap.put("/login", "anon");
filterMap.put("/level1/*", "authc");
filterMap.put("/level2/*", "authc");
filterMap.put("/level3/*", "authc");
shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
```



#### 编写接收用户输入的controller

```java
@PostMapping("/login")
public String login(String username, String password, String[] remme, Model model) {
    // 获取当前用户
    Subject subject = SecurityUtils.getSubject();
    // 封装用户登陆数据
    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

    if (remme != null && remme[0].equals("rememberme")) {
        usernamePasswordToken.setRememberMe(true);
    }

    // 执行登陆的方法
    try {
        subject.login(usernamePasswordToken);
    } catch (UnknownAccountException e) {
        model.addAttribute("msg", "用户名不存在");
        return "form";
    } catch (IncorrectCredentialsException e) {
        model.addAttribute("msg", "密码错误");
        return "form";
    } catch (AuthenticationException e) {
        e.printStackTrace();
    }

    return "index";
}
```



#### 修改之前LevelUserRealm的认证逻辑

```java
// 认证
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    System.out.println("认证");

    UsernamePasswordToken token1 = (UsernamePasswordToken) token;
    // 获取用户输入的用户名
    String username = token1.getUsername();

    LevelUser levelUser = levelUserService.getLevelUserByName(username);
    if (levelUser == null) {
        // return null自动抛出UnknownAccountException异常，认证失败
        return null;
    }

    return new SimpleAuthenticationInfo(levelUser,      // 授权可以获取这个用户的信息
                                        levelUser.getPassword(),
                                        "");
}
```



#### 修改授权逻辑

```java
// 授权
@Override
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    System.out.println("授权");

    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    Subject subject = SecurityUtils.getSubject();
    LevelUser levelUser = (LevelUser) subject.getPrincipal();       // 要获取LevelUser，就要在认证时添加LevelUser

    simpleAuthorizationInfo.addStringPermissions(Arrays.asList(levelUser.getRole().split(",")));

    return simpleAuthorizationInfo;
}
```



#### 退出编写推出登录controller

```java
@RequestMapping("/logout")
public String logout() {
    Subject subject = SecurityUtils.getSubject();
    subject.logout();
    return "index";
}
```



### 加密

```java
String password = "123456";
String salt = new SecureRandomNumberGenerator().nextBytes().toString();
int times = 2;  // 加密次数：2
String alogrithmName = "md5";   // 加密算法

String encodePassword = new SimpleHash(alogrithmName, password, salt, times).toString();

System.out.printf("原始密码是 %s , 盐是： %s, 运算次数是： %d, 运算出来的密文是：%s ",password,salt,times,encodePassword);
```



