package com.duongw.apigatewayservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
@Slf4j
public class PublicRoutes {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final String[] WHITE_LIST = {
            "/api/v1/auth/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui/index.html",
            "/actuator/**",
            "/api/v1/auth/**",
            "/api/v1/config-view/**",
            "/api/v1/internal/**"
    };

    public String[] getWhiteList() {
        return WHITE_LIST;
    }

    public boolean isWhiteListed(String path) {
        for (String whitePath : WHITE_LIST) {
            if (path.matches(whitePath)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWhiteListedRegex(String path) {
        log.info("PUBLIC_ROUTER -> IS_WHITE_LISTED_REGEX");

        for (String pattern : WHITE_LIST) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }

        return false;
    }
}
