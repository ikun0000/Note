package org.example.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(* org.example.service.UserServiceImpl.*(..))")
    public void pc() {}

    @Before("pc()")
    void before() {
        System.out.println("=========before=========");
    }

    @AfterReturning("pc()")
    void after() {
        System.out.println("=========after=========");
    }

    @Around("pc()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("==============around1==============");
//        proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("==============around2==============");
        return proceed;
    }

}
