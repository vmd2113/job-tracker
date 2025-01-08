package com.duongw.commonservice.model.dto.request.user;

import com.duongw.common.validator.enumvalidator.phonevalidator.PhoneNumber;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @PhoneNumber
    private String phoneNumber;


    private String firstName;

    private String lastName;

    private Long departmentId;


}
