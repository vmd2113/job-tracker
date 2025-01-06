package com.duongw.authservice.service.auth;

public interface IAuthService {

    TokenResponse signIn(SignInRequest signInRequest);
    TokenResponse signUp(SignUpRequest signUpRequest);
    TokenResponse refreshToken(HttpServletRequest request);

    String forgotPassword(String email);
    String resetPassword(String email);
    String changePassword(String email);
    String changeEmail(String email);

}
