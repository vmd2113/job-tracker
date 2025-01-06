package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.user.CreateUserRequest;
import com.duongw.commonservice.model.dto.request.user.UpdateUserRequest;
import com.duongw.commonservice.model.dto.response.user.UserDetailDTO;
import com.duongw.commonservice.model.dto.response.user.UserResponseDTO;

import java.util.List;

public interface IUserService {
    List<UserResponseDTO> getAllUser();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO getUserByPhoneNumber(String phoneNumber);

    UserResponseDTO createUser(CreateUserRequest user);

    UserResponseDTO updateUser(Long id, UpdateUserRequest user);

    UserResponseDTO updateUserStatus(Long id, String status);

    void deleteUser(Long id);

    UserDetailDTO getUserDetail(Long id);

    UserDetailDTO getUserDetailByUsername(String username);

    UserDetailDTO registerUser(CreateUserRequest user);

    UserDetailDTO updatePassword(String username, String password);
}




