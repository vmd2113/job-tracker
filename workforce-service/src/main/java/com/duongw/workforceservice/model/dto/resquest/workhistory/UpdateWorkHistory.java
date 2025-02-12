package com.duongw.workforceservice.model.dto.resquest.workhistory;

import com.duongw.workforceservice.model.entity.Works;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkHistory {

    private Long workId;

    private String workCode;

    private String workContent;


}
