package com.prolifera.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ProliferaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProliferaApplication.class, args);
	}

}
