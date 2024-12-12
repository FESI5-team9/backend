package com.fesi.mukitlist.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("먹킷리스트")
				.version("1.0.0")
				.description("먹킷리스트 API 명세서"))
			.components(new Components().addSecuritySchemes("bearerAuth",
				new SecurityScheme()
					.name("Authorization")
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT"))
			);
	}
}
