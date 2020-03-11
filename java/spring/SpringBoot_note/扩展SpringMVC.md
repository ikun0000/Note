### 扩展SpringMVC

实现WebMvcConfigurer并标注为配置类

```java
@Configuration
@EnableWebMvc   // 全面接管SpringMVC
public class ProjectConfiguration implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // 自定义视图解析器
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 添加视图
        registry.addViewController("/hello").setViewName("test");
    }
}
```

 

### 分析`@EnableWebMvc`

点进`@EnableWebMvc`

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
```

再进入`DelegatingWebMvcConfiguration.class`

```java
@Configuration(proxyBeanMethods = false)
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {

	private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();


	@Autowired(required = false)
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addWebMvcConfigurers(configurers);
		}
	}

	....
```

他继承了`WebMvcConfigurationSupport`

找到`WebMvcAutoConfiguration`

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = "";

	private static final String[] SERVLET_LOCATIONS = { "/" };
    
    ...
```

会发现`@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)`这个注解



因为开启`@EnableWebMvc`就会导入`DelegatingWebMvcConfiguration`类

而`DelegatingWebMvcConfiguration`继承了`WebMvcConfigurationSupport`

在mvc自动配置类标注了`@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)`，即没有这个bean才加载自动配置

所以自动配置失效





### 加载properties文件

`@PropertyResource`用于加载一个properties文件，springboot默认只加载application.yml，application.properties文件，如果有多个文件需要导入，在配置类上加上`@PropertySource`导入，导入之后可以配合` @ConfigurationProperties `和`@Value`使用

properties file

```properties
user.name="aaa"
user.password="123456"
```

configuration class

```java
@Configuration
@PropertySource(value = {"classpath:userprop.properties"})
public class MVCConfig {
    @Value("${user.name}")
    private String name;
    @Value("${user.password}")
    private String password;
}
```



### 加载Spring配置文件

springboot可以使用SpringMVC的applicationContext.xml配置bean，springboot使用` @ImportResource `导入springMVC的配置文件

applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:c="http://www.springframework.org/schema/c"
    xmlns:util="http://www.springframework.org/schema/util"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">


    <bean id="student" class="com.example.entity.Student">
        <property name="name" value="test"></property>
        <property name="age" value="20"></property>
    </bean>

</beans>
```

configuration class

```java
@Configuration
@ImportResource(value = {"classpath:applicationContext.xml"})
public class MVCConfig {
}
```



### Spring boot注册三大组件



#### 注册servlet

继承servlet

```java
@Component
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        // TODO
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

注册servlet

```java
@Configuration
public class MVCConfig {

    @Autowired
    private MyServlet myServlet;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(myServlet, "/test");	// 传入servlet和映射路径
        return bean;
    }

}
```



#### 注册过滤器

实现filter

```java
@Component
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
```

注册filter

```java
@Configuration
public class MVCConfig {

    @Autowired
    private MyFilter myFilter;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(myFilter);		// 设置filter
        bean.addUrlPatterns("/*");		// 添加拦截路径
        return bean;
    }
}
```



#### 注册监听器

实现监听器

```java
@Component
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // TODO
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO
    }
}
```

注册Listener

```java
@Configuration
public class MVCConfig {

    @Autowired
    private MyServletContextListener myServletContextListener;

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<EventListener> bean = new ServletListenerRegistrationBean<>();
        bean.setListener(myServletContextListener);
        return bean;
    }
}
```

