package com.duongw.workforceservice.model.dto.response.works;

import com.duongw.workforceservice.model.entity.WorkType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Setter
public class WorkResponseDTO {


    private Long worksId;

    private String workCode;

    private String workContent;

    private Long workTypeId;

    private String workTypeName;

    private Long priorityId;

    private String priorityName;

    private Long status;

    private String statusName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime finishTime;

    private Long assignedUserId;
}
