package com.example.springjunitpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringJunitPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJunitPracticeApplication.class, args);
	}

}
