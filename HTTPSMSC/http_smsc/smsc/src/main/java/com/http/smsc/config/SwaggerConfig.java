package com.http.smsc.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig 
{
	@Bean
	public OpenAPI apiInfo()
	{
		return new OpenAPI()
				.info(new Info()
						.title("Http smsc api")
						.description("http_smsc")
						.version("1.0"));
	}
	
	@Bean
	public GroupedOpenApi publicApi()
	{
		return GroupedOpenApi.builder()
				.group("http_smsc")
				.packagesToScan("com.http.smsc.controller")
				.build();
	}

}
