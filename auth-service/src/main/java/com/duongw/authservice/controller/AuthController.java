package com.duongw.authservice.controller;

import com.duongw.authservice.model.dto.request.SignInRequest;
import com.duongw.authservice.service.auth.AuthService;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("AUTH_CONTROLLER  -> signIn");
        TokenResponse token = authService.signIn(signInRequest);
        ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.OK, "Sign in successful", token);
        return ResponseEntity.ok(apiResponse);
    }
}
