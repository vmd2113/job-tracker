package com.duongw.commonservice.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Common Service API Documentation")
                        .version("1.0.0")
                        .description("API Documentation for Common Service")
                        .license(new License()
                                .name("API license")
                                .url("https://domain.vn/license"))
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")  // URL của API Gateway
                                .description("Gateway Server"),
                        new Server()
                                .url("http://localhost:8001")  // URL trực tiếp của Common Service
                                .description("Direct Common Service")
                ));
    }


}
