package com.duongw.workforceservice.model.dto.resquest.workhistory;

import com.duongw.workforceservice.model.entity.Works;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkHistory {

    private Long workId;

    private String workCode;

    private String workContent;


}
