package com.webrecruiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories (basePackages="com.webrecruiter.repository")
@SpringBootApplication
public class WebRecruiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebRecruiterApplication.class, args);
	}
}
