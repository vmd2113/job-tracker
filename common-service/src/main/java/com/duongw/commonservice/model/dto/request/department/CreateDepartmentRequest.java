package com.duongw.commonservice.model.dto.request.department;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartmentRequest {
    @NotBlank(message = "Department code cannot be empty")

    private String departmentCode;

    private String departmentName;

    private Long departmentParentId;

    private Long status;
}
