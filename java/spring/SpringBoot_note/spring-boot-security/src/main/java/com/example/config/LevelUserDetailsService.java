package com.example.config;

import com.example.dao.LevelUserService;
import com.example.entity.LevelUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class LevelUserDetailsService implements UserDetailsService {

    @Autowired
    private LevelUserService levelUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LevelUser levelUser = levelUserService.getLevelUserByName(username);
        if (levelUser == null) {
            return null;
        }

        return new User(username,       // 用户名
                levelUser.getPassword(),    // 密码
                true,               // 该用户是否开启
                true,       // 用户没有过期
                true,   // 用户证书没有过期
                true,       // 用户没有被锁定
                AuthorityUtils.commaSeparatedStringToAuthorityList(levelUser.getRole()));   // 用户有哪些权限
    }
}
