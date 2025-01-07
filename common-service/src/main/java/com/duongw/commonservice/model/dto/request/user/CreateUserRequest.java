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
    @NotBlank(message = "{validate.email.fail}")
    private String email;

    private String password = "$2a$10$dfNiXyI1oq/xY7PGkGB5E.E.1tyNjsXthXJgTf2nymmtE70kX2yW.";

    @PhoneNumber(message = "{validate.phone-number.fail}")
    @Size(max = 11, message = "{validate.phone-number.fail}")
    private String phoneNumber;

    @NotBlank(message = "{validate.first-name.required}")
    @Size(max = 100, message = "{validate.first-name.required}")
    private String firstName;


    @Size(max = 100, message = "{validate.last-name.required}")
    @NotBlank(message = "{validate.last-name.required}")
    private String lastName;


    private Long departmentId;

    @NotBlank(message = "{validate.status.required}")
    private String status = "Active";


}
