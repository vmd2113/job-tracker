package com.duongw.commonservice.model.dto.response.department;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentTreeResponseDTO {
    private Long departmentId;

    private String departmentName;

    private String departmentCode;

    private Long departmentParentId;

    private String departmentParentName;

    private String departmentParentCode;

    private Long status;

    private List<DepartmentTreeResponseDTO> children;
}
