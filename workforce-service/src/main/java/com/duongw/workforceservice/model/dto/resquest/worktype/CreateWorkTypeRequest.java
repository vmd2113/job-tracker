package com.duongw.workforceservice.model.dto.resquest.worktype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateWorkTypeRequest {

    @NotBlank(message = "workTypeCode is required")
    private String workTypeCode;

    @NotBlank(message = "workTypeName is required")
    private String workTypeName;

    private Long priorityId;

    @Positive(message = "processTime must be positive")
    private Float processTime;

    private Long status = 1L;


}
