package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.user.RegisterUserRequest;
import com.duongw.commonservice.model.dto.request.user.UpdateUserRequest;
import com.duongw.commonservice.model.dto.response.user.UserResponseDTO;

import java.util.List;

public interface IUserService {
    List<UserResponseDTO> getAllUser();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO getUserByPhoneNumber(String phoneNumber);

    UserResponseDTO createUser(RegisterUserRequest user);

    UserResponseDTO updateUser(Long id, UpdateUserRequest user);

    UserResponseDTO updateUserStatus(Long id, String status);

    void deleteUser(Long id);


}
