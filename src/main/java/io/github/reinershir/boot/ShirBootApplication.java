package io.github.reinershir.boot;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.github.reinershir.auth.annotation.EnableAuthentication;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"io.github.reinershir"})
@MapperScan(value = {"io.github.reinershir.boot.mapper","io.github.reinershir.ai.mapper"})
@EnableAuthentication
@Slf4j
public class ShirBootApplication {
	
	public static void main(String[] args) throws UnknownHostException {
	    SpringApplication app=new SpringApplication(ShirBootApplication.class);
        ConfigurableApplicationContext application=app.run(args);
		Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t"+
                        "Doc: \thttp://{}:{}/doc.html\n"+
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
        
	}
	

}
