package com.duongw.commonservice.model.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
