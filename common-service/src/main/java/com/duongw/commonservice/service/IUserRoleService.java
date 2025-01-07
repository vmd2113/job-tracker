package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.response.userrole.UserRoleResponseDTO;
import com.duongw.commonservice.model.entity.UserRole;

import java.util.List;

public interface IUserRoleService {
    UserRoleResponseDTO getUserRoleById(Long id);
    List<Long> getRoleByUserId(Long userId);


    Long save(UserRole userRole);
}
