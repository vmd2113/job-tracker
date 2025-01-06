package com.duongw.commonservice.model.dto.response.user;

import com.duongw.commonservice.model.dto.response.role.RoleResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailDTO {
    private Long userId;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private Long departmentId;

    private Long status;

    private List<RoleResponse> roles;
}
