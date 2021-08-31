package com.burlywoodpalette.bwvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BWVideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BWVideoApplication.class, args);
	}

}
