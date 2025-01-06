package com.duongw.apigatewayservice.filter;

import com.duongw.apigatewayservice.config.PublicRoutes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${jwt.access}")
    private String secretKey;

    private final PublicRoutes publicRoutes;

    public JwtAuthenticationFilter(PublicRoutes publicRoutes) {
        this.publicRoutes = publicRoutes;
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Kiểm tra xem đường dẫn có nằm trong white list không
        if (publicRoutes.isWhiteListedRegex(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        if (token == null) {
            logger.warn("Authorization header is missing or malformed");
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }
        if (!isValidToken(token)) {
            logger.warn("Invalid JWT token: {}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = getClaims(token);
        if (claims == null) {
            logger.error("Failed to extract claims from JWT token: {}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Đảm bảo các claims an toàn
        String userId = claims.get("id", String.class);
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        if (roles == null) {
            roles = new ArrayList<>(); // hoặc một giá trị mặc định
        }

        // Log thông tin người dùng khi xác thực thành công
        logger.info("JWT token validated for user: {} with roles: {}", username, String.join(",", roles));

        // Thêm các header vào request để phân quyền trong các microservice
        exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-Username", username)
                .header("X-Roles", String.join(",", roles))
                .build();

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7); // Trả về token mà không có "Bearer "
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error("Failed to extract claims from JWT token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
