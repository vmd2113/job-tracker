package com.duongw.apigatewayservice.config;

import com.duongw.apigatewayservice.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthFilter) {
        return builder.routes()
                // Route cho common service - CẦN BẢO MẬT
                .route("common-service", r -> r.path("/api/v1/cm/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://COMMON-SERVICE"))

                // Route cho Workforce Service - CẦN BẢO MẬT
                .route("workforce-service", r -> r.path("/api/v1/wfm/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://WORKFORCE-SERVICE"))

                // Route cho auth API (public, không cần JWT)
                .route("auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                // Route cho internal API (không cần JWT)
                .route("internal-api", r -> r.path("/api/v1/internal/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho Swagger UI (không cần JWT)
                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho OpenAPI docs (không cần JWT)
                .route("api-docs", r -> r.path("/v3/api-docs/**")
                        .uri("lb://COMMON-SERVICE"))

                // Route cho Swagger resources (không cần JWT)
                .route("swagger-resources", r -> r.path("/swagger-resources/**")
                        .uri("lb://COMMON-SERVICE"))

                .build();
    }
}