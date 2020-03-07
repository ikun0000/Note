package com.example.demo;

import com.example.demo.providers.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private Provider provider;

	@Test
	void contextLoads() throws InterruptedException {
		provider.send("aaa");
		Thread.sleep(3000);
		provider.send("bbb");
	}

}
