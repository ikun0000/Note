package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepositoryBean() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);     // 不在执行建表语句
        return jdbcTokenRepository;
    }

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private LevelUserDetailsService levelUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()        // 对请求进行认证
                .antMatchers("/level1/*").hasAnyAuthority("level1", "admin")
                .antMatchers("/level2/*").hasAnyAuthority("level2", "admin")
                .antMatchers("/level3/*").hasAnyAuthority("level3", "admin")
                .antMatchers("/", "/index", "/index.html", "/form", "/constraint", "/logout").permitAll()      // 允许放行的URI
                .anyRequest().authenticated();       // 所有请求都要认证

        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        http.formLogin()         // 使用表单验证
                .loginPage("/form")         // 定义认证页
                .loginProcessingUrl("/constraint")      // 定义认证逻辑的URI
                .failureHandler(myAuthenticationFailureHandler)
                .successForwardUrl("/index")
                .and()
                .csrf().disable();          // 关闭CSRF否则表单登陆失败

        http.logout()           // 定义注销
                .logoutUrl("/logout")       // 注销的URL
                .logoutSuccessUrl("/index")     // 注销成功后跳转到的地址
                .deleteCookies("JSRSESSION")        // 注销删除COOKIE
                .clearAuthentication(true);

        http.sessionManagement()        // 开启session管理
                .maximumSessions(5)     // 一个用户最多持有的session数量
                .expiredUrl("/form")    // session失效后跳转地址
                .maxSessionsPreventsLogin(true);    // 如果session达到maximumSessions就不允许登陆

        http.rememberMe()       // 添加记住我功能
                .tokenRepository(persistentTokenRepositoryBean())    // 配置 token 持久化仓库
                .tokenValiditySeconds(3600)           // remember 过期时间，单为秒
                .userDetailsService(levelUserDetailsService)    // 处理自动登录逻辑
                .rememberMeParameter("remember_me");            // 表单name属性
    }
}
