package com.duongw.authservice.model.dto.response;

import com.duongw.common.model.dto.response.RoleResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CurrentUser {

    private Long userId;

    private String username;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private Long departmentId;

    private String departmentName;

    private Long status;

    private List<RoleResponseDTO> roles;
}
