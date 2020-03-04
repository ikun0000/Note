package org.example;

import static org.junit.Assert.assertTrue;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.StaffMapper;
import org.example.pojo.Staff;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    public void testMybatis() throws IOException {
//        String resource = "mybatis-config.xml";
//        InputStream in = Resources.getResourceAsStream(resource);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
//        SqlSession sqlSession = sqlSessionFactory.openSession(true);
//        StaffMapper mapper = sqlSession.getMapper(StaffMapper.class);
//
//        List<Staff> staffList = mapper.findAll();
//
//        System.out.println(staffList);
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StaffMapper userMapper = (StaffMapper) context.getBean("staffMapper");

        List<Staff> all = userMapper.findAll();
        for (Staff staff : all) {
            System.out.println(staff);
        }

    }
}
