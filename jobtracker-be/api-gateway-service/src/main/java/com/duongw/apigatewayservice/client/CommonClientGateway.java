package com.duongw.apigatewayservice.client;

import com.duongw.apigatewayservice.model.dto.reponse.ConfigViewResponseGatewayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "common-service", url = "http://localhost:8001" + "/api/v1/internal")
public interface CommonClientGateway {
    @GetMapping(path = "/config-view/role")
    List<ConfigViewResponseGatewayDTO> getConfigViewByRoleId(@RequestParam(name = "roleId") String roleId);





}
