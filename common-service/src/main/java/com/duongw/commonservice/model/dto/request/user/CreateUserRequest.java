package com.duongw.commonservice.model.dto.request.user;

import com.duongw.common.validator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "{validate.username.fail}")
    private String username;
    @Email(message = "{validate.email.fail}")
    private String email;


    private String password;

    @PhoneNumber(message = "{validate.phone-number.fail}")
    private String phoneNumber;

    @NotBlank(message = "{validate.first-name.required}")
    private String firstName;

    @NotBlank(message = "{validate.last-name.required}")
    private String lastName;


    private Long departmentId;

    @NotBlank(message = "{validate.status.required}")
    private String status;


}
