package org.example;

import static org.junit.Assert.assertTrue;

import org.example.pojo.Student;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

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
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfiguration.class);

        Student student = context.getBean("student", Student.class);
        System.out.println(student.getName());

    }
}
