package com.duongw.commonservice.model.dto.request.user;

import com.duongw.common.validator.enumvalidator.phonevalidator.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private Long departmentId;

    private String status = "Active";


}
