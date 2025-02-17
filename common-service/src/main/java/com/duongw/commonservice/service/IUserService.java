package com.duongw.commonservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.user.*;
import com.duongw.commonservice.model.dto.response.user.UserDetailDTO;
import com.duongw.commonservice.model.dto.response.user.UserResponseDTO;
import org.springframework.transaction.annotation.Transactional;

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

    UserResponseDTO changeUserPassword(Long id, ChangePasswordRequest changePasswordRequest);

    UserDetailDTO changeUserInfo(Long id, UpdateUserInfoRequest updateUserInfoRequest);

    void deleteUser(Long id);
    void deleteUserList(List<Long> ids);

    UserDetailDTO getUserDetail(Long id);

    UserDetailDTO getUserDetailByUsername(String username);

    @Transactional
    UserDetailDTO registerUser(RegisterRequest user);


    PageResponse<?> searchUserByCriteria(

            int pageNo,
            int pageSize,

            String usernameSearch,
            String emailSearch,
            String phoneNumberSearch,
            String firstNameSearch,

            String sortBy,
            String sortDirection

    );


}




