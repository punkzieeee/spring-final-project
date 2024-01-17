package com.punkzieeee.apigateaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "API Getaway", version = "1.0"))
@SpringBootApplication
public class ApiGateawayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGateawayApplication.class, args);
	}
}
