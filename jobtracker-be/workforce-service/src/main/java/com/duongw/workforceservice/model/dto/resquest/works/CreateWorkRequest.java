package com.duongw.workforceservice.model.dto.resquest.works;

import com.duongw.workforceservice.model.entity.WorkHistory;
import com.duongw.workforceservice.model.entity.WorkType;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;


    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    private Long assignedUserId;




}
