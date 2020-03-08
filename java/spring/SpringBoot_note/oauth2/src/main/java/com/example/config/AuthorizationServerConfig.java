package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

@Order(1)
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer);
        tokenEnhancers.add(jwtAccessTokenConverter);
        chain.setTokenEnhancers(tokenEnhancers);

        endpoints.authenticationManager(authenticationManager)     // 配置认证管理器
                .tokenStore(tokenStore)        // 配置令牌存储
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(chain);
        //        .userDetailsService(userDetailSservice);          // 配置UserDetailsService，配置了自定义用户认证要加上
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()       // 从内存配置应用
                .withClient("test")     // client_id
                .secret(new BCryptPasswordEncoder().encode("test1234"))             // client_secret
                .accessTokenValiditySeconds(3600)       // token过期时间
                .refreshTokenValiditySeconds(864000)    // 刷新token的过期时间
                .scopes("all", "a", "b", "c")           // 令牌的作用域
                .authorizedGrantTypes("password", "refresh_token")       // 支持的认证方式
                .and()
                .withClient("test2")
                .secret(new BCryptPasswordEncoder().encode("test2222"))
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(864000)
                .redirectUris("http://localhost:8080/")
                .scopes("all", "b", "c", "d")
                .authorizedGrantTypes("password", "code", "refresh_token");
    }
}
