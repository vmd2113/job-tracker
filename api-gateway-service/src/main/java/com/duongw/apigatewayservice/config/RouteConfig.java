package com.duongw.apigatewayservice.config;

import com.duongw.apigatewayservice.filter.JwtAuthenticationFilter;
import com.duongw.apigatewayservice.token.JwtService;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtFilter) {
        return builder.routes()
                // Route cho config-view API
                .route("config-view", r -> r.path("/api/v1/config-view/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho departments API
                .route("departments", r -> r.path("/api/v1/departments/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho users API
                .route("users", r -> r.path("/api/v1/users/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho user-role API
                .route("user-role", r -> r.path("/api/v1/user-role/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho categories API
                .route("categories", r -> r.path("/api/v1/categories/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho auth API (public, không cần JWT)
                .route("auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho OpenAPI docs
                .route("api-docs", r -> r.path("/v3/api-docs/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho Swagger resources
                .route("swagger-resources", r -> r.path("/swagger-resources/**")
                        .uri("lb://COMMON-SERVICE"))



                .build();
    }

    @Bean
    public GlobalFilter authenticationFilter(PublicRoutes publicRoutes, JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService, publicRoutes);
    }
}
