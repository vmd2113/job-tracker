package com.duongw.commonservice.controller.internal;


import com.duongw.common.constant.ApiPath;
import com.duongw.commonservice.model.dto.response.configview.ConfigViewResponseDTO;
import com.duongw.commonservice.service.impl.ConfigViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_PATH_VERSION + "/internal/config-view")
@RequiredArgsConstructor
@Slf4j
public class ConfigViewInternalController {

    private final ConfigViewService configViewService;

    @GetMapping(path = "/role")
    public List<ConfigViewResponseDTO> getConfigViewByRoleCode(@RequestParam(name = "roleCodes") List<String> roleCodes) {
        log.info("CONFIG_VIEW_CONTROLLER  -> getConfigViewByRoleCode" + roleCodes);
        return configViewService.getConfigViewByRoleCode(roleCodes);
    }

}
