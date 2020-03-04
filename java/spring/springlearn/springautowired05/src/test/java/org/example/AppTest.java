package org.example;

import static org.junit.Assert.assertTrue;

import org.example.pojo.Human;
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
    public void testBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Human human = context.getBean("human", Human.class);

        human.getDog().say();
        human.getCat().say();

    }
}
