package com.duongw.common.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    private List<String> roles;


}
