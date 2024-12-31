package com.duongw.commonservice.model.dto.request.configview;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateConfigViewRequest {

    private String viewName;

    private String viewPath;

    private String apiPath;

    private String roleId;

    private Long status;
}
