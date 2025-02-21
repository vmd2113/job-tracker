package com.duongw.commonservice.model.dto.request.user;

import com.duongw.common.validator.enumvalidator.phonevalidator.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdatePhoneNumberRequest {

    @PhoneNumber
    private String phoneNumber;

    @PhoneNumber
    private String confirmPhoneNumber;
}
