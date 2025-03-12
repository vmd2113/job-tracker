package com.duongw.apigatewayservice.model.dto.reponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConfigViewResponseGatewayDTO {
    private Long id;

    private String viewName;

    private String viewPath;

    private String apiPath;

    private String roleId;

    private Long status;
}
