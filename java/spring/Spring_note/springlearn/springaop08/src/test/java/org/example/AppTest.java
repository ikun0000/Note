package org.example;

import static org.junit.Assert.assertTrue;

import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testAOP() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        UserService userServiceImpl = (UserService) context.getBean("userServiceImpl");

        userServiceImpl.add();
        userServiceImpl.delete();

    }
}
