package com.duongw.workforceservice.model.dto.resquest.worktype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateWorkTypeRequest {

    @NotBlank(message = "workTypeCode is required")
    @Size(min = 3, max = 100, message = "workTypeCode must be between 3 and 100 characters")
    private String workTypeCode;


    @NotBlank(message = "workTypeName is required")
    @Size(min = 3, max = 500, message = "workTypeName must be between 3 and 100 characters")
    private String workTypeName;

    private Long priorityId;

    @Positive(message = "processTime must be positive")
    @NotBlank(message = "processTime is required")
    private Float processTime;

    private Long status = 1L;


}
