package com.duongw.authservice.config;

import com.duongw.authservice.filters.JwtAuthenticationFilter;
import com.duongw.authservice.service.users.CustomUserDetailService;
import com.duongw.common.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
public class AuthSecurityConfig {

    private final CustomUserDetailService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public AuthSecurityConfig(CustomUserDetailService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("SECURITY_CONFIG  -> authenticationProvider");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(getPasswordEncoder());

        return provider;


    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        log.info("SECURITY_CONFIG  -> authenticationManager");
        return configuration.getAuthenticationManager();

    }




    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("SECURITY_CONFIG  -> securityFilterChain");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(SystemConstant.WHITE_LIST).permitAll();
                    auth.requestMatchers("/api/v1/auth/**").permitAll();
                    auth.anyRequest().authenticated();
                });

        return http.build();
    }

}
