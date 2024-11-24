package com.fesi.mukitlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MukitlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(MukitlistApplication.class, args);
	}

}
