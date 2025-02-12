package com.duongw.workforceservice.model.dto.resquest.workconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkConfigRequest {

    private Long workTypeId;
    private Long priorityId;
    private Long oldStatus;
    private Long newStatus;
}
