package com.duongw.common.constant;

public class SystemConstant {

    public static final String X_REQUEST_ID = "X-Request-Id";
    public static final String X_USER_ID = "X-User-Id";
    public static final String X_USER_NAME = "X-User-Name";
    public static final String X_USER_ROLES = "X-User-Roles";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ACCESS_TOKEN = "access_token";


    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String RESET_PASSWORD_TOKEN = "reset_password_token";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String SCOPE = "scope";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String RESPONSE_TYPE = "response_type";
    public static final String STATE = "state";


    final String LIKE_FORMAT = "%%%s%%";
    final String SORT_BY = "(\\w+?)(:)(.*)";
    final String SEARCH_OPERATOR = "(\\w+?)(:|<|>|>=|<=)(.*)";

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


}
