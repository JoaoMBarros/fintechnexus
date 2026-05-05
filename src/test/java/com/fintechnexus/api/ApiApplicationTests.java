package com.fintechnexus.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
