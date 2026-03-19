package com.hanaro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		// Swagger UI에서 JWT 토큰 입력할 수 있게 설정
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.name("Authorization");

		return new OpenAPI()
			.info(new Info()
				.title("하나로 예적금몰 API")
				.description("하나로 예적금몰 서비스 API 문서")
				.version("1.0.0"))
			.addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
			.components(new Components()
				.addSecuritySchemes("Bearer Token", securityScheme));
	}
}
