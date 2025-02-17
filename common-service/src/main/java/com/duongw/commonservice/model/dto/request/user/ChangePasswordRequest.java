package com.duongw.commonservice.model.dto.request.user;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

}
