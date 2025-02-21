package com.duongw.authservice.service.auth;

import com.duongw.authservice.client.CommonClient;
import com.duongw.authservice.model.dto.request.SignInRequest;
import com.duongw.authservice.model.dto.request.SignUpRequest;
import com.duongw.authservice.model.dto.response.CurrentUser;


import com.duongw.authservice.model.entity.AuthUserDetail;
import com.duongw.authservice.service.jwt.JwtService;
import com.duongw.common.config.i18n.Translator;
import com.duongw.common.enums.TokenType;
import com.duongw.common.exception.InvalidDataException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.RoleResponseDTO;
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
            throw new InvalidDataException("Authentication failed");

        }

        log.info("User {} authenticated successfully", user.getUsername());

        // create new access token
        String accessToken = jwtService.generateToken(user);

        // create new refresh token
        String refreshToken = jwtService.generateRefreshToken(user);

        // save token to db


//        tokenCacheService.saveRefreshToken(user.getUserId(), refreshToken, 7 * 24 * 60 * 60);

        // Set token vào cookie
//        setTokenCookies(accessToken, refreshToken);

        log.info("Token saved to cookies for user {}", user.getUsername());


        log.info("::::::::::::::::::::::::TOKEN SAVED TO COOKIE::::::::::::::::::::::::::::::");


        return TokenResponse.builder()
                .accessToken(accessToken)
                .username(user.getUsername())
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .accessTokenExpiration(jwtService.getAccessTokenExpiration())
                .refreshTokenExpiration(jwtService.getRefreshTokenExpiration())
                .roles(roles)
                .build();

    }


    @Override
    public TokenResponse signUp(SignUpRequest signUpRequest) {
        AuthUserDetail user = commonClient.signUp(signUpRequest);
        log.info("USER_SERVICE  -> signUp");

        String accessToken = jwtService.generateToken(user);
        log.info("Access token generated for user {}", user.getUsername());

        // create new refresh token
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Refresh token generated for user {}", user.getUsername());
        List<RoleResponseDTO> roles = commonClient.getRoleByUserId(user.getUserId());


        return TokenResponse.builder()
                .accessToken(accessToken)
                .username(user.getUsername())
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .accessTokenExpiration(jwtService.getAccessTokenExpiration())
                .refreshTokenExpiration(jwtService.getRefreshTokenExpiration())
                .roles(roles)
                .build();
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request) {
        return null;
    }

    @Override
    public String logOut(String token) {
        //TODO: implement redis blacklist token
        return "Success";
    }

    @Override
    public CurrentUser getCurrentUser(String token) {

        log.info("AUTH_SERVICE  -> getCurrentUser");
        try {
            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
            log.info("USERNAME: {}", username);

            AuthUserDetail user = commonClient.getUserDetailByUsername(username).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
            log.info("USER: {}", user);

            List<RoleResponseDTO> roles = commonClient.getRoleByUserId(user.getUserId());
            user.setRoles(roles);
            log.info("ROLES: {}", roles);
            log.info("USER: {}", user);

            CurrentUser currentUser = convertToCurrentUser(user);
            log.info("CURRENT_USER: {}", currentUser);
            return currentUser;

        } catch (Exception e) {
            log.error("AUTH_SERVICE  -> getCurrentUser fail");
            throw new RuntimeException(e);
        }


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

    private CurrentUser convertToCurrentUser(AuthUserDetail user) {
        return CurrentUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .departmentId(user.getDepartmentId())
                .departmentName(user.getDepartmentName())
                .status(user.getStatus())
                .roles(user.getRoles())
                .build();
    }
}
