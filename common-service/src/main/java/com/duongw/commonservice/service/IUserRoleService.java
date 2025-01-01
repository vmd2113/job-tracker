package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.response.userrole.UserRoleResponseDTO;

public interface IUserRoleService {
    UserRoleResponseDTO getUserRoleById(Long id);
    Long getRoleByUserId(Long userId);


}
