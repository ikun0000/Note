package org.example;

import static org.junit.Assert.assertTrue;

import org.example.pojo.Student;
import org.example.pojo.User;
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
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void testBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Student student = (Student) context.getBean("student");
        User user = context.getBean("userc", User.class);

        System.out.println(user);

        System.out.println(student);
    }
}
