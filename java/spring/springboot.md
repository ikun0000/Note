### SpringBoot开启事务

1. 在Application类或者有`@Configuration`类加上`@EnableTransactionManagement `
2. 在service的方法上写`@Transactional`注解

