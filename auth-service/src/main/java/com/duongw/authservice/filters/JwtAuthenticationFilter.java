package com.duongw.authservice.filters;


import com.duongw.authservice.service.users.CustomUserDetailService;
import com.duongw.common.constant.SystemConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.duongw.common.constant.SystemConstant.X_USER_NAME;
import static com.duongw.common.constant.SystemConstant.X_USER_ROLES;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;

    @Autowired
    public JwtAuthenticationFilter(CustomUserDetailService userDetailService) {

        this.userDetailService = userDetailService;
    }

    /**
     * Check if the request URI is part of the whitelist.
     */
    private boolean isWhiteList(HttpServletRequest request) {
        String path = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        return Arrays.stream(SystemConstant.WHITE_LIST)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * Extract token from request header or cookies.
     */
//    private String extractToken(HttpServletRequest request) {
//        // Check header first
//        String authorization = request.getHeader(AUTHORIZATION);
//        if (StringUtils.isNotBlank(authorization) && authorization.startsWith(SystemConstant.BEARER_PREFIX)) {
//            return authorization.substring(SystemConstant.BEARER_PREFIX.length());
//        }
//
//        // Check cookies if not found in header
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            return Arrays.stream(cookies)
//                    .filter(cookie -> SystemConstant.ACCESS_TOKEN.equals(cookie.getName()))
//                    .map(Cookie::getValue)
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        return null;
//    }

    /**
     * Set the authentication in the security context.
     */
//    private void setAuthentication(String token, HttpServletRequest request) {
//        String userName = jwtService.extractUsername(token, TokenTypes.ACCESS_TOKEN);
//        if (StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailService.loadUserByUsername(userName);
//            if (jwtService.isValid(token, TokenTypes.ACCESS_TOKEN, userDetails)) {
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("AUTHSERVICE --> doFilterInternal");
        // 1. Skip filter for whitelisted paths
        if (isWhiteList(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Get user info from request headers (provided by API Gateway)
        String username = request.getHeader(X_USER_NAME);
        String roles = request.getHeader(X_USER_ROLES);

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(roles)) {
            // Create user authentication context
            UserDetails userDetails = userDetailService.loadUserByUsername(username); // Optional: Verify from DB
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. Proceed with the filter chain
        filterChain.doFilter(request, response);
    }


}
