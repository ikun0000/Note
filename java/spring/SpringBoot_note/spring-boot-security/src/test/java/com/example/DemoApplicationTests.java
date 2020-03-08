package com.example;

import com.example.dao.LevelUserService;
import com.example.entity.LevelUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private LevelUserService levelUserService;

	@Test
	void contextLoads() {
		LevelUser aa = levelUserService.getLevelUserByName("cc");
		System.out.println(aa);

		LevelUser zz = levelUserService.getLevelUserByName("zz");
		System.out.println(zz);
	}

	@Test
	void getPassword() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		System.out.println(passwordEncoder.encode("111"));
		System.out.println(passwordEncoder.encode("222"));
		System.out.println(passwordEncoder.encode("333"));
		System.out.println(passwordEncoder.encode("444"));
		System.out.println(passwordEncoder.encode("555"));
		System.out.println(passwordEncoder.encode("666"));
		System.out.println(passwordEncoder.encode("toor"));
	}

}
