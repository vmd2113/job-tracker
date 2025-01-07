package com.duongw.commonservice.model.dto.response.role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {

    private Long roleId;
    private String roleName;
    private String roleCode;


}
