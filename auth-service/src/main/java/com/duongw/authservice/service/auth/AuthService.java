package com.duongw.authservice.service.auth;

import com.duongw.authservice.model.dto.request.SignInRequest;
import com.duongw.authservice.model.dto.request.SignUpRequest;
import com.duongw.common.model.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public class AuthService implements IAuthService {
    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        return null;
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
