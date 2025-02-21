package com.duongw.authservice.service.jwt;


import com.duongw.authservice.model.entity.AuthUserDetail;
import com.duongw.authservice.service.users.CustomUserDetailService;
import com.duongw.common.exception.InvalidDataException;
import com.duongw.common.enums.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access}")
    private String accessSecret;

    @Value("${jwt.refreshKey}")
    private String refreshSecret;

    @Value("${jwt.resetPassword}")
    private String resetPasswordSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${jwt.reset-password-expiration}")
    private long resetPasswordExpiration;

    @Value("${jwt.issuer}")
    private String issuer;

    private Map<TokenType, Key> keys;

    @PostConstruct
    public void init() {
        keys = new EnumMap<>(TokenType.class);
        keys.put(TokenType.ACCESS_TOKEN, Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret)));
        keys.put(TokenType.REFRESH_TOKEN, Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret)));
        keys.put(TokenType.RESET_PASSWORD_TOKEN, Keys.hmacShaKeyFor(Decoders.BASE64.decode(resetPasswordSecret)));
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return buildToken(createClaims(userDetails), userDetails, TokenType.ACCESS_TOKEN, accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, TokenType.REFRESH_TOKEN, refreshTokenExpiration);
    }

    @Override
    public String generateResetPasswordToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, TokenType.RESET_PASSWORD_TOKEN, resetPasswordExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails,
                              TokenType tokenType, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(keys.get(tokenType), SignatureAlgorithm.HS512)
                .compact();
    }

    private Map<String, Object> createClaims(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", extractUserId(userDetails));
        claims.put("roles", extractRoles(userDetails));

        return claims;
    }

    private String extractUserId(UserDetails userDetails) {
        if (userDetails instanceof AuthUserDetail) {
            return ((AuthUserDetail) userDetails).getUserId().toString();
        }
        return userDetails.getUsername();
    }

    private List<String> extractRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails) {
        try {
            final String username = extractUsername(token, type);
            return username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token, type) &&
                    validateToken(token, type);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    @Override
    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    @Override
    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public boolean validateToken(String token, TokenType type) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keys.get(type))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw new InvalidDataException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new InvalidDataException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new InvalidDataException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            throw new InvalidDataException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("JWT claims string is empty");
        }
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parserBuilder()
                .setSigningKey(keys.get(type))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration).before(new Date());
    }
}