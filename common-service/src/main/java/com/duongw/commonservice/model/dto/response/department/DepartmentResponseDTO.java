package com.duongw.commonservice.model.dto.response.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDTO {

    private Long departmentId;

    private String departmentName;

    private String departmentCode;

    private Long departmentParentId;

    private String departmentParentName;

    private Long status;



}
