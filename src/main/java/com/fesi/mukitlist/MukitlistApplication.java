package com.fesi.mukitlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	info = @Info(
		title = "먹킷리스트",
		version = "v1",
		description = "먹킷리스트 API"
	)
)
@SpringBootApplication
public class MukitlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(MukitlistApplication.class, args);
	}

}
