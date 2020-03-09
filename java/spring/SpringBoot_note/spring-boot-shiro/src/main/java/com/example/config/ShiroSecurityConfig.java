package com.example.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.LinkedHashMap;

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
