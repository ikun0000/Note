package com.example.demo;

import com.example.demo.provider.ErrorProvider;
import com.example.demo.provider.InfoProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private ErrorProvider errorProvider;

	@Autowired
	private InfoProvider infoProvider;

	@Test
	void  contextLoads() {

		infoProvider.send("testing info message");

		errorProvider.send("testing error message");

	}

}
