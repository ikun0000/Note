### SpringBoot集成OAuth2

导入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
</dependency>
```



#### 开启Oauth2

在配置类上加上`@EnableAuthorizationServer`注解

```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends WebSecurityConfigurerAdapter {
}
```



配置application.yml

```yaml
security:
  oauth2:
    client:
      client-id: test
      client-secret: test1234
      registered-redirect-uri: http://localhost:8080/
```



#### 授权码获取令牌

访问URL请求授权码

```
http://localhost:8080/oauth/authorize?response_type=code&client_id=test&redirect_uri=http://localhost:8080/&scope=all&state=hello
```

会进行一次登陆，然后会跳到授权页面，授权会跳回到redirect_uri

```
http://localhost:8080/?code=cpfDSC&state=hello
```



使用code请求token

curl访问

```shell
curl --location --request POST 'http://localhost:8080/oauth/token?grant_type=authorization_code&code=K6uUuD&client_id=test&redirect_uri=http://localhost:8080/&scope=all' \
--header 'Authorization: Basic dGVzdDp0ZXN0MTIzNA=='
```

OkHttp访问

```java
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("text/plain");
RequestBody body = RequestBody.create(mediaType, "");
Request request = new Request.Builder()
  .url("http://localhost:8080/oauth/token?grant_type=authorization_code&code=K6uUuD&client_id=test&redirect_uri=http://localhost:8080/&scope=all")
  .method("POST", body)
  .addHeader("Authorization", "Basic dGVzdDp0ZXN0MTIzNA==")
  .build();
Response response = client.newCall(request).execute();
```

header的Authorization的为Basic加上client_id和client_secret的base64编码



返回token

```
{
    "access_token": "9d319aa5-35fe-4e04-a9d5-cfaf6e2646a6",
    "token_type": "bearer",
    "refresh_token": "2b8b9ef9-2d89-43a9-8b15-a57b37bc3542",
    "expires_in": 43199,
    "scope": "all"
}
```



#### 密码模式获取令牌

curl访问

```shell
curl --location --request POST 'http://localhost:8080/oauth/token?grant_type=password&username=user&password=1d29b8c7-637f-4417-963b-ef8f63618e12&scope=all' \
--header 'Authorization: Basic dGVzdDp0ZXN0MTIzNA=='
```

密码模式要求提交登陆的用户名和密码

OkHttp访问

```java
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("text/plain");
RequestBody body = RequestBody.create(mediaType, "");
Request request = new Request.Builder()
  .url("http://localhost:8080/oauth/token?grant_type=password&username=user&password=1d29b8c7-637f-4417-963b-ef8f63618e12&scope=all")
  .method("POST", body)
  .addHeader("Authorization", "Basic dGVzdDp0ZXN0MTIzNA==")
  .build();
Response response = client.newCall(request).execute();
```



#### 开启资源服务器获取资源

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig {
}
```

定义资源

```java
@RestController
public class HelloController {

    @GetMapping("/info")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

}
```



获取资源

curl

```shell
curl --location --request GET 'http://localhost:8080/info' \
--header 'Authorization: bearer 1a777fd5-357d-484e-b3b2-cfd9e6e7c927'
```

OkHttp

```java
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
Request request = new Request.Builder()
  .url("http://localhost:8080/info")
  .method("GET", null)
  .addHeader("Authorization", "bearer 1a777fd5-357d-484e-b3b2-cfd9e6e7c927")
  .build();
Response response = client.newCall(request).execute();
```

```json
{
    "authorities": [],
    "details": {
        "remoteAddress": "0:0:0:0:0:0:0:1",
        "sessionId": null,
        "tokenValue": "1a777fd5-357d-484e-b3b2-cfd9e6e7c927",
        "tokenType": "bearer",
        "decodedDetails": null
    },
    "authenticated": true,
    "userAuthentication": {
        "authorities": [],
        "details": {
            "grant_type": "password",
            "username": "user",
            "scope": "all"
        },
        "authenticated": true,
        "principal": {
            "password": null,
            "username": "user",
            "authorities": [],
            "accountNonExpired": true,
            "accountNonLocked": true,
            "credentialsNonExpired": true,
            "enabled": true
        },
        "credentials": null,
        "name": "user"
    },
    "clientOnly": false,
    "oauth2Request": {
        "clientId": "test",
        "scope": [
            "all"
        ],
        "requestParameters": {
            "grant_type": "password",
            "username": "user",
            "scope": "all"
        },
        "resourceIds": [],
        "authorities": [
            {
                "authority": "ROLE_USER"
            }
        ],
        "approved": true,
        "refresh": false,
        "redirectUri": null,
        "responseTypes": [],
        "extensions": {},
        "grantType": "password",
        "refreshTokenRequest": null
    },
    "principal": {
        "password": null,
        "username": "user",
        "authorities": [],
        "accountNonExpired": true,
        "accountNonLocked": true,
        "credentialsNonExpired": true,
        "enabled": true
    },
    "credentials": "",
    "name": "user"
}
```



>  在同时定义了认证服务器和资源服务器后，再去使用授权码模式获取令牌可能会遇到 Full authentication is required to access this resource 的问题，这时候只要确保认证服务器先于资源服务器配置即可，比如在认证服务器的配置类上使用`@Order(1)`标注，在资源服务器的配置类上使用`@Order(2)`标注。 



### 自定义令牌配置

实现认证服务器

```java
@Order(1)
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);     // 配置认证管理器
        //        .userDetailsService(userDetailSservice);          // 配置UserDetailsService，配置了自定义用户认证要加上
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()       // 从内存配置应用
                .withClient("test")     // client_id
                .secret("test1234")             // client_secret
                .accessTokenValiditySeconds(3600)       // token过期时间
                .refreshTokenValiditySeconds(864000)    // 刷新token的过期时间
                .scopes("all", "a", "b", "c")           // 令牌的作用域
                .authorizedGrantTypes("password")       // 支持的认证方式
                .and()
                .withClient("test2")
                .secret("test2222")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(864000)
                .redirectUris("http://localhost:8080/")
                .scopes("all", "b", "c", "d")
                .authorizedGrantTypes("password", "code");
    }
}
```

添加AuthenticationManager的bean

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 从内存中配置用户
        auth.inMemoryAuthentication()
                .withUser("root")
                .password(new BCryptPasswordEncoder().encode("toor"))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .authorities("admin");
    }
}
```

这样配置以后application.yml的配置就失效了



#### 配置Redis存储令牌

```java
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

}
```

在`AuthorizationServerConfig`添加：
```java
@Autowired
private TokenStore tokenStore;

@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager)     // 配置认证管理器
        .tokenStore(tokenStore);        // 配置令牌存储
}
```



#### 使用JWT替换原有令牌

```java
@Configuration
public class TokenStoreConfig {
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("test_key");      // 签发密钥
        return jwtAccessTokenConverter;
    }

}
```

在`AuthorizationServerConfig`添加：

```java
@Autowired
private TokenStore tokenStore;

@Autowired
private JwtAccessTokenConverter jwtAccessTokenConverter;

@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager)     // 配置认证管理器
        .tokenStore(tokenStore)        // 配置令牌存储
        .accessTokenConverter(jwtAccessTokenConverter);
    //        .userDetailsService(userDetailSservice);          // 配置UserDetailsService，配置了自定义用户认证要加上
}
```



#### 扩展JWT

实现`TokenEnhancer`，在enhance中添加额外信息

```java
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1000);
        map.put("msg", "nothing");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
        return accessToken;
    }
}
```

添加这个bean

```java
@Bean
public TokenEnhancer tokenEnhancer() {
    return new JwtTokenEnhancer();
}
```

 在认证服务器里配置该增强器： 

```java
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
}
```



### Java解析JWT

导入jjwt

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

```java
@GetMapping("/index")
public Claims index(@AuthenticationPrincipal Authentication authentication, HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String token = StringUtils.substringAfter(header, "bearer ");
    System.out.println(token);
    return Jwts.parser()
        .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
        .parseClaimsJws(token)
        .getBody();
}
```





### 刷新令牌

在`authorizedGrantTypes`加上`refresh_token`

```java
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
        .authorizedGrantTypes("password", "code");
}
```



curl

```shell
curl --location --request POST 'http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtc2ciOiJub3RoaW5nIiwiY29kZSI6MTAwMCwidXNlcl9uYW1lIjoicm9vdCIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiIzYzQ0YjZmZS01ZWY5LTQ4MzQtOWI4MS0xN2IzMTA5Njc2MGMiLCJleHAiOjE1ODQ1MTUxNTcsImF1dGhvcml0aWVzIjpbImFkbWluIl0sImp0aSI6ImM2MDg2MDI2LThlNzctNDhmNi05MDE5LWQ4ZGJiZWVlNDkzZCIsImNsaWVudF9pZCI6InRlc3QifQ.PKKcC9KQZu2kG6LFkVypxXazPzeeXrZoyR39GSNNPKE' \
--header 'Authorization: Basic dGVzdDp0ZXN0MTIzNA=='
```

OkHttp

```java
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("text/plain");
RequestBody body = RequestBody.create(mediaType, "");
Request request = new Request.Builder()
  .url("http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtc2ciOiJub3RoaW5nIiwiY29kZSI6MTAwMCwidXNlcl9uYW1lIjoicm9vdCIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiIzYzQ0YjZmZS01ZWY5LTQ4MzQtOWI4MS0xN2IzMTA5Njc2MGMiLCJleHAiOjE1ODQ1MTUxNTcsImF1dGhvcml0aWVzIjpbImFkbWluIl0sImp0aSI6ImM2MDg2MDI2LThlNzctNDhmNi05MDE5LWQ4ZGJiZWVlNDkzZCIsImNsaWVudF9pZCI6InRlc3QifQ.PKKcC9KQZu2kG6LFkVypxXazPzeeXrZoyR39GSNNPKE")
  .method("POST", body)
  .addHeader("Authorization", "Basic dGVzdDp0ZXN0MTIzNA==")
  .build();
Response response = client.newCall(request).execute();
```

