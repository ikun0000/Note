package com.example;

import com.example.dao.LevelUserService;
import com.example.entity.LevelUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private LevelUserService levelUserService;

	@Test
	public void contextLoads() {
		LevelUser aa = levelUserService.getLevelUserByName("cc");
		System.out.println(aa);

		LevelUser zz = levelUserService.getLevelUserByName("zz");
		System.out.println(zz);
	}

	@Test
	void testShiro() {
		Subject currentUser = SecurityUtils.getSubject();

		Session session = currentUser.getSession();
		session.setAttribute("someKey", "aValue");
	}

}
