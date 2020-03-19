package com.example.client;

import org.slf4j.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.*;
import org.springframework.context.*;

@SpringBootApplication
public class ClientApplication {


	public static void main(String[] args) {

		SpringApplicationBuilder builder = new SpringApplicationBuilder(ClientApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}



	private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);

}
