package com.duongw.commonservice.service.impl;

import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.commonservice.model.dto.response.userrole.UserRoleResponseDTO;
import com.duongw.commonservice.model.entity.UserRole;
import com.duongw.commonservice.repository.UserRoleRepository;
import com.duongw.commonservice.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService implements IUserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    private UserRoleResponseDTO convertToUserRoleResponseDTO(UserRole userRole) {
        UserRoleResponseDTO userRoleResponseDTO = new UserRoleResponseDTO();
        userRoleResponseDTO.setUserRoleId(userRole.getId());
        userRoleResponseDTO.setUserId(userRole.getUserId());
        userRoleResponseDTO.setRoleId(userRole.getRoleId());
        return userRoleResponseDTO;
    }
    @Override
    public UserRoleResponseDTO getUserRoleById(Long id) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("UserRole not found"));
        return convertToUserRoleResponseDTO(userRole);
    }

    @Override
    public Long getRoleByUserId(Long userId) {
        UserRole userRole = userRoleRepository.findByUserId(userId);
        if (userRole == null) {
            throw new ResourceNotFoundException("UserRole not found");
        }
        return userRole.getRoleId();

    }
}
