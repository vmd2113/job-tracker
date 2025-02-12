package com.duongw.workforceservice.model.dto.response.workconfig;

import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.entity.WorkType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Setter
public class WorkConfigResponseDTO {

    private Long workConfigId;

    private String workTypeName;
    private WorkTypeResponseDTO workType;

    private Long priorityId;



    private Long oldStatus;

    private Long newStatus;
}
