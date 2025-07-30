package com.madgum.spring_security_learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true,securedEnabled = true)
public class SpringSecurityLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityLearningApplication.class, args);
	}

}
