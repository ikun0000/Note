package org.example.mapper;

import org.apache.ibatis.session.SqlSession;
import org.example.pojo.Staff;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class StaffMapperImpl extends SqlSessionDaoSupport implements StaffMapper {

    @Override
    public List<Staff> findAll() {
        return getSqlSession().getMapper(StaffMapper.class).findAll();
    }
}
