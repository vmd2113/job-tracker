package com.duongw.authservice.model.dto.request;

import com.duongw.common.validator.enumvalidator.phonevalidator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Phone number is required")
    @PhoneNumber(message = "Phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String confirmPassword;
}
