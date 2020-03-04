package org.example;

import static org.junit.Assert.assertTrue;

import org.example.pojo.Hello;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
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
    public void testIoc() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Hello hello = (Hello) context.getBean("hello2");
        System.out.println(hello);
    }
}
