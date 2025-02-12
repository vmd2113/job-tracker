package com.duongw.workforceservice.model.dto.resquest.works;

import com.duongw.workforceservice.model.entity.WorkHistory;
import com.duongw.workforceservice.model.entity.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkRequest {


    private String workCode;

    private String workContent;

    private Long workTypeId;

    private Long priorityId;

    private Long status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long assignedUserId;




}
