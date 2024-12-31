package com.duongw.commonservice.model.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserResponseDTO {

    private Long userId;


    private String username;


    private String email;


    private String phoneNumber;


    private String firstName;


    private String lastName;


    private Long departmentId;


    private Long status;
}
