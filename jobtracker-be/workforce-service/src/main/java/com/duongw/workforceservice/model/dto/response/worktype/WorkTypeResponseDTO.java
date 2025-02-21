package com.duongw.workforceservice.model.dto.response.worktype;

import jakarta.persistence.Column;
import lombok.*;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class WorkTypeResponseDTO {
    private Long workTypeId;

    private String workTypeCode;

    private String workTypeName;

    private Long priorityId;

    private Float processTime;

    private Long status;


}
