package com.duongw.commonservice.validator;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.commonservice.model.dto.request.user.CreateUserRequest;
import com.duongw.commonservice.model.dto.request.user.RegisterRequest;
import com.duongw.commonservice.model.dto.request.user.UpdateUserInfoRequest;
import com.duongw.commonservice.model.dto.request.user.UpdateUserRequest;
import com.duongw.commonservice.model.entity.Users;
import com.duongw.commonservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j

public class UserValidator {
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Kiểm tra trùng lặp thông tin user
     *
     * @param username      Username cần kiểm tra
     * @param email         Email cần kiểm tra
     * @param phoneNumber   Số điện thoại cần kiểm tra
     * @param excludeUserId ID của user đang update (null nếu là create)
     * @throws com.duongw.common.exception.AlreadyExistedException nếu thông tin bị trùng
     */
    public void validateDuplicateInfo(String username, String email, String phoneNumber, Long excludeUserId) {
        log.info("Validating duplicate info for user. ExcludeId: {}", excludeUserId);

        // Kiểm tra username
        Users existingUserByUsername = userRepository.findByUsername(username);
        if (existingUserByUsername != null &&
                (excludeUserId == null || !existingUserByUsername.getUserId().equals(excludeUserId))) {
            throw new AlreadyExistedException(Translator.toLocate("validate.username.exist"));
        }

        // Kiểm tra email
        Users existingUserByEmail = userRepository.findByEmail(email);
        if (existingUserByEmail != null &&
                (excludeUserId == null || !existingUserByEmail.getUserId().equals(excludeUserId))) {
            throw new AlreadyExistedException(Translator.toLocate("validate.email.exist"));
        }

        // Kiểm tra số điện thoại
        Users existingUserByPhone = userRepository.findByPhoneNumber(phoneNumber);
        if (existingUserByPhone != null &&
                (excludeUserId == null || !existingUserByPhone.getUserId().equals(excludeUserId))) {
            throw new AlreadyExistedException(Translator.toLocate("validate.phone-number.exist"));
        }

        log.info("Duplicate info validation passed");
    }

    /**
     * Validate toàn bộ thông tin user khi tạo mới
     */
    public void validateCreateUser(CreateUserRequest request) {
        log.info("Validating create user request");
        try {
            validateDuplicateInfo(request.getUsername(), request.getEmail(), request.getPhoneNumber(), null);
        } catch (AlreadyExistedException e) {
            throw new AlreadyExistedException(e.getMessage());
        }
    }

    /**
     * Validate toàn bộ thông tin user khi cập nhật
     */
    public void validateUpdateUser(UpdateUserRequest request, Long userId) {
        log.info("Validating update user request for userId: {}", userId);
        validateDuplicateInfo(request.getUsername(), request.getEmail(), request.getPhoneNumber(), userId);
    }

    public void validateChangeUserInfo(UpdateUserInfoRequest request, Long userId) {
        log.info("Validating change user info request for userId: {}", userId);
        validateDuplicateInfo(null, request.getEmail(), request.getPhoneNumber(), userId);
    }

    public void validateRegisterUser(RegisterRequest request) {
        log.info("Validating register user request");
        validateDuplicateInfo(request.getUsername(), request.getEmail(), request.getPhoneNumber(), null);
    }
}
