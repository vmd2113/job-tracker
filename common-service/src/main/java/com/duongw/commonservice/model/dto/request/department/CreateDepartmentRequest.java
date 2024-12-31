package com.duongw.commonservice.model.dto.request.department;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartmentRequest {

    private String departmentName;

    private String departmentCode;

    private Long departmentParentId;

    private Long status;
}
