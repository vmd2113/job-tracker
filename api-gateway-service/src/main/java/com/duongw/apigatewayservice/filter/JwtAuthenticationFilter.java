package com.duongw.apigatewayservice.filter;

import com.duongw.apigatewayservice.config.PublicRoutes;
import com.duongw.apigatewayservice.token.JwtService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final PublicRoutes publicRoutes;

    public JwtAuthenticationFilter(JwtService jwtService, PublicRoutes publicRoutes) {
        this.jwtService = jwtService;
        this.publicRoutes = publicRoutes;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("API-GATEWAY-SERVICE --> JWT FILTER");
        String path = exchange.getRequest().getURI().getPath();

        if (publicRoutes.isWhiteListedRegex(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        if (token == null) {
            logger.warn("Authorization header is missing or malformed");
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        if (!jwtService.isValidToken(token)) {
            logger.warn("Invalid JWT token: {}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtService.getClaims(token);
        if (claims == null) {
            logger.error("Failed to extract claims from JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String userId = claims.get("userId", String.class);
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        if (roles == null) {
            roles = new ArrayList<>();
        }

        logger.info("JWT token validated for user: {} with roles: {}", username, String.join(",", roles));

        // Tạo request mới
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-Username", username)
                .header("X-Roles", String.join(",", roles))
                .build();

        // Log kiểm tra headers
        HttpHeaders headers = mutatedRequest.getHeaders();
        logger.info("Added custom headers:");
        logger.info("X-User-Id: {}", headers.getFirst("X-User-Id"));
        logger.info("X-Username: {}", headers.getFirst("X-Username"));
        logger.info("X-Roles: {}", headers.getFirst("X-Roles"));

        // Tạo exchange mới và tiếp tục chuỗi filter
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}