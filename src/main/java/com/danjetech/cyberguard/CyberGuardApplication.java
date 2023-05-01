package com.danjetech.cyberguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAsync
public class CyberGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyberGuardApplication.class, args);
	}

}
