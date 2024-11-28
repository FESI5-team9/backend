package com.fesi.mukitlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		String securitySchemeName = "bearerAuth";
		return new OpenAPI()
			.info(new Info()
				.title("먹킷리스트")
				.version("1.0.0")
				.description("먹킷리스트 API 명세서"));
	}
}
