package com.sapient.data;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class DataRetrievalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataRetrievalServiceApplication.class, args);
	}
}
