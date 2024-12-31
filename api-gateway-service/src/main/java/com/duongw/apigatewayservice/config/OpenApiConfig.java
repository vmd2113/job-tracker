package com.duongw.apigatewayservice.config;

import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Tên nhóm API (có thể là tên module hoặc dịch vụ)

    @Bean
    public GroupedOpenApi commonServiceApi() {
        return GroupedOpenApi.builder()
                .group("common-service")
                .pathsToMatch("/api/v1/**")
                .build();
    }


}
