package com.nextlabs.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Sarita Sethy
 */
@SpringBootApplication
public class LoggerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LoggerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LoggerApplication.class);
	}

}
