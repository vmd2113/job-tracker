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
    private Long workTypeId;


    private Long priorityId;
    private String priorityName;


    private Long oldStatusId;
    private String oldStatusName;

    private Long newStatusId;
    private String newStatusName;
}
