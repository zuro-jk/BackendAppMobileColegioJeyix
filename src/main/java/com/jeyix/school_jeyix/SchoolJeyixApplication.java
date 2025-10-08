package com.jeyix.school_jeyix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class SchoolJeyixApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolJeyixApplication.class, args);
	}

}
