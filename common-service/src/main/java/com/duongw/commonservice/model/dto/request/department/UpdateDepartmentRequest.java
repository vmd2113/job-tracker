package com.duongw.commonservice.model.dto.request.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDepartmentRequest {

    private String departmentName;

    private String departmentCode;

    private Long departmentParentId;

    private Long status;
}
