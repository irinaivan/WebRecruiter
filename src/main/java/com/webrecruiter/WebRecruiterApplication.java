package com.webrecruiter;

import com.webrecruiter.utils.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories (basePackages="com.webrecruiter.repository")
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@SpringBootApplication
public class WebRecruiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebRecruiterApplication.class, args);
	}
}
