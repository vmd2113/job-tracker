package com.duongw.commonservice.model.dto.request.user;

import com.duongw.common.validator.enumvalidator.phonevalidator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank
    private String username;

    @Email
    private String email;

    @PhoneNumber
    private String phoneNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;


    private Long departmentId;

    private Long status;


}
