package com.duongw.workforceservice.model.dto.response.workhistory;

import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Setter
public class WorkHistoryResponseDTO {

    private WorkResponseDTO work;
    private String workCode;
    private String workContent;

}
