package com.duongw.apigatewayservice.client;

import com.duongw.apigatewayservice.model.dto.reponse.ConfigViewResponseGatewayDTO;
import com.duongw.apigatewayservice.model.dto.reponse.ItemResponseGateway;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(
        name = "common-service-internal",
        url = "http://localhost:8001/api/v1/internal"
)
public interface CommonClientGateway {

    @GetMapping(path = "/config-view/role")
    List<ConfigViewResponseGatewayDTO> getConfigViewByRoleCode(@RequestParam(name = "roleCodes") List<String> roleCodes);
    // ITEM
    @GetMapping(path = "/items/code")
    ItemResponseGateway getItemByCode(@RequestParam(name = "code") String code);



}
