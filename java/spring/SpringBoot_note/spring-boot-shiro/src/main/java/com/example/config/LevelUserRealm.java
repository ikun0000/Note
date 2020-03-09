package com.example.config;

import com.example.dao.LevelUserService;
import com.example.entity.LevelUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class LevelUserRealm extends AuthorizingRealm {

    @Autowired
    private LevelUserService levelUserService;

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
}
