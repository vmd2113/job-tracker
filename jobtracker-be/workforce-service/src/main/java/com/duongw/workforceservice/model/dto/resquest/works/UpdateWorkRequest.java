package com.duongw.workforceservice.model.dto.resquest.works;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkRequest {

    private String workCode;

    private String workContent;

    private Long workTypeId;

    private Long priorityId;

    private Long status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime finishTime;

    private Long assignedUserId;

}
