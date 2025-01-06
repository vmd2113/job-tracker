package com.duongw.apigatewayservice.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {


    @Value("${jwt.access}")
    private String secretKey;

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Failed to extract claims from JWT token: {}", e.getMessage());
            return null;
        }
    }

}
