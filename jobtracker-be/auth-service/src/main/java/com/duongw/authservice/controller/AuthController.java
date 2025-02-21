package com.duongw.authservice.controller;

import com.duongw.authservice.model.dto.request.SignInRequest;
import com.duongw.authservice.model.dto.request.SignUpRequest;
import com.duongw.authservice.model.dto.response.CurrentUser;
import com.duongw.authservice.model.entity.AuthUserDetail;
import com.duongw.authservice.service.auth.AuthService;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiPath.API_AUTH)
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<TokenResponse>> signIn(@RequestBody SignInRequest signInRequest) {
        try {

            log.info("AUTH_CONTROLLER  -> signIn");
            TokenResponse token = authService.signIn(signInRequest);
            ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.OK, "Sign in successful", token);
            return ResponseEntity.ok(apiResponse);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }


    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestBody SignUpRequest signUpRequest) {
        log.info("AUTH_CONTROLLER  -> signUp");
        TokenResponse token = authService.signUp(signUpRequest);
        ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.OK, "Sign up successful", token);
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<ApiResponse<?>> createUser() {
        log.info("AUTH_CONTROLLER  -> createUser");
//        UserDetailDTO userDetailDTO = authService.registerUser(user);
//        ApiResponse<UserDetailDTO> apiResponse = new ApiResponse<>(HttpStatus.OK, "Create user successful", userDetailDTO);
//        return ResponseEntity.ok(apiResponse);
        return null;
    }

    public ResponseEntity<ApiResponse<?>> logOut(){
        log.info("AUTH_CONTROLLER  -> logOut");
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/current-user")

    public ResponseEntity<ApiResponse<?>> getCurrentUser(HttpServletRequest request) {
        log.info("AUTH_CONTROLLER  -> getCurrentUser");
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CurrentUser user = authService.getCurrentUser(token);
        ApiResponse<CurrentUser> apiResponse = new ApiResponse<>(HttpStatus.OK, "Get current user successful", user);
        return ResponseEntity.ok(apiResponse);


    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

