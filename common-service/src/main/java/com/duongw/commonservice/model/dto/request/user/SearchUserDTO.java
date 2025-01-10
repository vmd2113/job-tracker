package com.duongw.commonservice.model.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchUserDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;

    private String sortBy;
    private String sortDirection;

}
