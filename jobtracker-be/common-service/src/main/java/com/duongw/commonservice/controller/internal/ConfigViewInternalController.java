package com.duongw.commonservice.controller.internal;


import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.commonservice.model.dto.response.configview.ConfigViewResponseDTO;
import com.duongw.commonservice.service.impl.ConfigViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_PATH_VERSION + "/internal/config-view")
@RequiredArgsConstructor
@Slf4j
public class ConfigViewInternalController {

    private final ConfigViewService configViewService;

    @GetMapping(path = "/role")
    public ResponseEntity<ApiResponse<?>> getConfigViewByRoleId(@RequestParam(name = "roleId") String roleId) {

        log.info("CONFIG_VIEW_CONTROLLER  -> getConfigViewByRoleId" + roleId);
        List<ConfigViewResponseDTO> configViewList = configViewService.getConfigViewByRoleId(roleId);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.get-by-role-id.success"), configViewList);
        return ResponseEntity.ok(apiResponse);
    }

}
