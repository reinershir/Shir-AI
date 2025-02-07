package io.github.reinershir.test;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.reinershir.boot.ShirBootApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ShirBootApplication.class)
public class CodeGeneretTester {

	@Autowired
	@Test
	public void runCodeGenerate() {
	}
}
