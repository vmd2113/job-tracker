package com.duongw.authservice.service.auth;

import com.duongw.authservice.client.CommonClient;
import com.duongw.authservice.model.dto.request.SignInRequest;
import com.duongw.authservice.model.dto.request.SignUpRequest;
import com.duongw.authservice.model.dto.response.RoleResponseDTO;
import com.duongw.authservice.model.entity.AuthUserDetail;
import com.duongw.authservice.service.jwt.JwtService;
import com.duongw.common.model.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthService implements IAuthService {

    private final CommonClient commonClient;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Autowired
    public AuthService(CommonClient commonClient, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.commonClient = commonClient;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        log.info("AUTH_SERVICE  -> signIn: username: {}, password: {} ", signInRequest.getUsername(), signInRequest.getPassword());
        AuthUserDetail user = commonClient.getUserDetailByUsername(signInRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("GET: user: {}", user);
        if (user == null) {
            log.warn(":::::::::::::::::::::::::::::: User not found: {}", signInRequest.getUsername());
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User found: {}", user.getUsername());
        log.info("GET: user.getRoles(): {}", user.getRoles());
        List<RoleResponseDTO> roles = commonClient.getRoleByUserId(user.getUserId());


        log.info("GET: roles: {}", roles);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(RoleResponseDTO::getRoleCode)
                .map(SimpleGrantedAuthority::new)
                .toList();
        log.info("GET: authorities: {}", authorities);
        //TODO: catch exception
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getUsername(),
                    signInRequest.getPassword(),
                    authorities
            ));
        } catch (Exception e) {
            log.error("AUTH_SERVICE  -> signIn: Authentication failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("User {} authenticated successfully", user.getUsername());

        // create new access token
        String accessToken = jwtService.generateToken(user);
        log.info("Access token generated for user {}", user.getUsername());

        // create new refresh token
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Refresh token generated for user {}", user.getUsername());

        // save token to db


//        tokenCacheService.saveRefreshToken(user.getUserId(), refreshToken, 7 * 24 * 60 * 60);

        // Set token vào cookie
//        setTokenCookies(accessToken, refreshToken);

        log.info("Token saved to cookies for user {}", user.getUsername());


        log.info("::::::::::::::::::::::::TOKEN SAVED TO COOKIE::::::::::::::::::::::::::::::");


        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .roles(user.getRoles().stream().map(RoleResponseDTO::getRoleCode).toList())
                .message("Sign in successful")
                .build();

    }

    @Override
    public TokenResponse signUp(SignUpRequest signUpRequest) {
        return null;
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request) {
        return null;
    }

    @Override
    public String forgotPassword(String email) {
        return "";
    }

    @Override
    public String resetPassword(String email) {
        return "";
    }

    @Override
    public String changePassword(String email) {
        return "";
    }

    @Override
    public String changeEmail(String email) {
        return "";
    }
}
