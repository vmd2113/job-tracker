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
    @Size(min = 6, max = 500, message = "{validate.username-length.fail}")
    private String username;

    @Email(message = "{validate.email.fail}")
    @Size(max = 500, message = "{validate.email-length.fail}")
    private String email;

    @PhoneNumber
    private String phoneNumber;

    @NotBlank(message = "{validate.firstname.fail}")
    private String firstName;

    @NotBlank(message = "{validate.lastname.fail}")
    @Size( max = 500, message = "{validate.lastname-length.fail}")
    private String lastName;

    private Long departmentId;

    private Long status;


}
