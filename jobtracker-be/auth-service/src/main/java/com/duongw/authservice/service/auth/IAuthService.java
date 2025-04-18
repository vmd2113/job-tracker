package com.duongw.authservice.service.auth;

import com.duongw.authservice.model.dto.request.SignInRequest;
import com.duongw.authservice.model.dto.request.SignUpRequest;
import com.duongw.authservice.model.dto.response.CurrentUser;
import com.duongw.authservice.model.entity.AuthUserDetail;
import com.duongw.common.model.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {

    TokenResponse signIn(SignInRequest signInRequest);
    TokenResponse signUp(SignUpRequest signUpRequest);
    TokenResponse refreshToken(HttpServletRequest request);

    String logOut(String token);




    CurrentUser getCurrentUser(String token);

    String forgotPassword(String email);
    String resetPassword(String email);
    String changePassword(String email);
    String changeEmail(String email);

}
