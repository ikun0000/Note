package org.example;

import static org.junit.Assert.assertTrue;

import org.example.mapper.DeptMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    @Autowired
    private DeptMapper deptMapper;

    @Test
    public void shouldAnswerWithTrue()
    {
        System.out.println(deptMapper.queryAll());
    }
}
