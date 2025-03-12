package com.duongw.apigatewayservice.config;

import com.duongw.apigatewayservice.client.CommonClientGateway;
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
                // Route cho common service
                .route("common-service", r -> r.path("/api/v1/cm/**")
                        .filters(f -> f.stripPrefix(0))  // Không strip prefix
                        .uri("lb://COMMON-SERVICE"))
                // Các route khác giữ nguyên


                // Route cho Workforce Service
                .route("workforce-service", r -> r.path("/api/v1/wfm/**")
                        .uri("lb://WORKFORCE-SERVICE"))

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
    public GlobalFilter authenticationFilter(PublicRoutes publicRoutes, JwtService jwtService, CommonClientGateway commonClientGateway) {
        return new JwtAuthenticationFilter(jwtService, publicRoutes, commonClientGateway);
    }
}
