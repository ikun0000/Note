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