package io.github.reinershir.test;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.reinershir.ai.service.ChatGPTService;
import io.github.reinershir.boot.ShirBootApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ShirBootApplication.class)
public class ChatgptTest {
	
	@Autowired
	ChatGPTService chatgptService;

	@Test
	public void testSSE() throws InterruptedException {
		chatgptService.completions("hello", "gpt-3.5-turbo");
		Thread.sleep(10000);
	}
}
