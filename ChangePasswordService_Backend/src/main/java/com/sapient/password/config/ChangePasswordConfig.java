package com.sapient.password.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.sapient.password.model.CustomPasswordEncoder;

@Configuration
public class ChangePasswordConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CustomPasswordEncoder customPasswordEncoder() {
		return new CustomPasswordEncoder();
	}
}
