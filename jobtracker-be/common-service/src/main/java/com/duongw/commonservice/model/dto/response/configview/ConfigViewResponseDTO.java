package com.duongw.commonservice.model.dto.response.configview;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ConfigViewResponseDTO {

    private Long id;

    private String viewName;

    private String viewPath;

    private String apiPath;

    private String roleId;

    private Long status;
}
