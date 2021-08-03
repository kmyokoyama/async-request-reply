package io.kmyokoyama.asyncrequestreply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class AsyncRequestReplyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncRequestReplyApplication.class, args);
	}

}
