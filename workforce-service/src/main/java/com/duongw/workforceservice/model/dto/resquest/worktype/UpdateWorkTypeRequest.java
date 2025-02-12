package com.duongw.workforceservice.model.dto.resquest.worktype;

import lombok.*;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkTypeRequest {


    private String workTypeCode;

    private String workTypeName;

    private Long priorityId;

    private Float processTime;

    private Long status;
}
