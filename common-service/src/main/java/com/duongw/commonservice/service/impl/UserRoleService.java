package com.duongw.commonservice.service.impl;

import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.commonservice.model.dto.response.userrole.UserRoleResponseDTO;
import com.duongw.commonservice.model.entity.UserRole;
import com.duongw.commonservice.repository.UserRoleRepository;
import com.duongw.commonservice.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
        log.info("USER_ROLE_SERVICE  -> getUserRoleById: {}", id);
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("UserRole not found"));
        return convertToUserRoleResponseDTO(userRole);
    }

    @Override
    public List<Long> getRoleByUserId(Long userId) {
        log.info("USER_ROLE_SERVICE  -> getRoleByUserId");
        List<UserRole> userRoleList = userRoleRepository.findAllByUserId(userId);
        List<Long> roleIdList = userRoleList.stream().map(UserRole::getRoleId).toList();
        log.info("GET: userIdList: {} ", roleIdList);
        return roleIdList;
    }

    @Override
    public Long save(UserRole userRole) {
        UserRole saveUserRole = userRoleRepository.save(userRole);
        log.info("UserRole save success: {}", saveUserRole);
        return saveUserRole.getId();
    }
}
