package com.sliit.betterwellness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SpringBootApplication(scanBasePackages = "com.sliit.betterwellness.services")
@SpringBootApplication
public class BetterWellnessApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetterWellnessApplication.class, args);
	}

}

