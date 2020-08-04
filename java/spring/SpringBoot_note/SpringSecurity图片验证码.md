# Spring Security 自定义图片验证码



## 准备工作

### pom依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```



### 登陆模板

```html
<!DOCTYPE html>

<html  xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
       xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
</head>
<body>
    <form method="post" action="/loginproc">
        <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
        username: <input type="text" name="username"><br>
        Passowrd: <input type="password" name="password"><br>
        <input type="submit" value="Authentication">
    </form>
</body>
</html>
```



### 测试用的Controller

```java
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String testAccess() {
        return "已经通过认证授权";
    }

    @RequestMapping("/loginpage")
    public String loginPage() {
        return "loginpage";
    }

}

```



### Spring Security 基本配置

```java
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/loginpage")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/loginproc")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .passwordParameter("password")
                .usernameParameter("username")
                .loginPage("/loginpage")
                .loginProcessingUrl("/loginproc");
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(new User("root",
                        passwordEncoder.encode("123456"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ADMIN")))
                .withUser(new User("user",
                        passwordEncoder.encode("654321"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")));
    }
}

```



## 开始写验证码功能

首先添加一个Bean用来专门存储验证码信息的实体类

```java
package com.example.demo.entity;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class ImageCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    public ImageCode() {
    }

    public ImageCode(BufferedImage image, String code, int expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    @Override
    public String toString() {
        return "ImageCode{" +
                "image=" + image +
                ", code='" + code + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}

```

有参数的构造函数的中传入的是过期的秒数，`expireTime` 的get和set方法被改造过，get方法被换成返回当前是否过期，set方法设置也是用秒数设置过期时间。



接着写生成验证码的controller

```java
package com.example.demo.controller;

import com.example.demo.entity.ImageCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@RestController
public class ImageCodeController {

    public static final String SESSION_KEY = "IMAGE_CODE";

    @GetMapping("/imagecode")
    public void imageCode(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        ImageCode imageCode = createImageCode();
        request.getSession().setAttribute(SESSION_KEY, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    private ImageCode createImageCode() {
        int width = 80;
        int height = 25;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        Random random = new Random();
        graphics.setColor(getRandColor(200, 250));
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font("Times New Roman",  Font.ITALIC, 23));
        graphics.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }

        String numberCode = "";
        for (int i = 0; i < 4; i++) {
            String number = String.valueOf(random.nextInt(10));
            numberCode += number;
            graphics.setColor(new Color(20 + random.nextInt(110),
                    20 + random.nextInt(110),
                    20 + random.nextInt(110)));
            graphics.drawString(number, 20 * i + 6, 20);
        }

        graphics.dispose();

        return new ImageCode(image, numberCode, 60);
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}

```

访问 `/imagecode` 时返回验证码并把验证码放到当前 `JSESSIONID` 的session中，另外两个函数用来生成验证码和生成随机干扰线条。



之后添加一个当验证码错误时的Exception类

```java
package com.example.demo.exception;

import org.springframework.security.core.AuthenticationException;

public class ImageCodeException extends AuthenticationException {
    public ImageCodeException(String explanation) {
        super(explanation);
    }
}

```

注意是 `org.springframework.security.core.AuthenticationException` 包下的 



然后写验证验证码的filter

```java
package com.example.demo.filter;

import com.example.demo.controller.ImageCodeController;
import com.example.demo.entity.ImageCode;
import com.example.demo.exception.ImageCodeException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ImageCodeAuthenticationFilter extends OncePerRequestFilter {

    public void vertify(HttpServletRequest request) throws ImageCodeException {
        ImageCode imageCode = (ImageCode) request.getSession().getAttribute(ImageCodeController.SESSION_KEY);
        String code = request.getParameter("imagecode");
        request.getSession().removeAttribute(ImageCodeController.SESSION_KEY);

        if (StringUtils.isEmpty(code)) {
            throw new ImageCodeException("验证码不能为空");
        }

        if (imageCode == null) {
            throw new ImageCodeException("验证码不存在");
        }

        if (imageCode.isExpried()) {
            throw new ImageCodeException("验证码以过期");
        }

        if (!StringUtils.equals(imageCode.getCode(), code)) {
            throw new ImageCodeException("验证码错误");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/loginproc", request.getRequestURI()) &&
                StringUtils.equalsIgnoreCase(request.getMethod(),"post")) {
            try {
                vertify(request);
            } catch (ImageCodeException exception) {
                response.sendRedirect("/loginpage");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

```

要实现 `OncePerRequestFilter` 类的 `doFilterInternal` 方法，如果验证通过则使用 `filterChain.doFilter(request, response)` 放行



然后修改登陆页面

```html
<!DOCTYPE html>

<html  xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
       xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
</head>
<body>
    <form method="post" action="/loginproc">
        <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
        username: <input type="text" name="username"><br>
        Passowrd: <input type="password" name="password"><br>
        <img src="/imagecode" />
        <input type="text" name="imagecode"><br>
        <input type="submit" value="Authentication">
    </form>
</body>
</html>
```



然后把自定义验证码的的filter加载 `UsernamePasswordAuthenticationFilter` 的前面

```java
package com.example.demo.config;

import com.example.demo.filter.ImageCodeAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/loginpage", "/imagecode")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/loginproc")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .passwordParameter("password")
                .usernameParameter("username")
                .loginPage("/loginpage")
                .loginProcessingUrl("/loginproc");

        http.addFilterBefore(new ImageCodeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(new User("root",
                        passwordEncoder.encode("123456"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("USER_ADMIN")))
                .withUser(new User("user",
                        passwordEncoder.encode("654321"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")));
    }
}

```



完成！