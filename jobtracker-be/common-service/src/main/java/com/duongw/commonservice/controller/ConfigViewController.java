package com.duongw.commonservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.commonservice.model.dto.request.configview.CreateConfigViewRequest;
import com.duongw.commonservice.model.dto.request.configview.UpdateConfigViewRequest;
import com.duongw.commonservice.model.dto.response.configview.ConfigViewResponseDTO;
import com.duongw.commonservice.service.IConfigViewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = ApiPath.API_CONFIG_VIEW)

public class ConfigViewController {


    private final IConfigViewService configViewService;

    @Autowired
    public ConfigViewController(IConfigViewService configViewService) {
        this.configViewService = configViewService;
    }

    @GetMapping(path = "/")
    @Operation(summary = "get all data of config view", description = "Send a request via this API to get all data of config view")

    public ResponseEntity<ApiResponse<?>> getAllConfigView() {
        List<ConfigViewResponseDTO> configViewList = configViewService.getAllConfigView();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.get-all.success"), configViewList);
        return ResponseEntity.ok(apiResponse);
    }




    @GetMapping(path = "/{id}")
    @Operation(summary = "get data of config view by id", description = "Send a request via this API to get data of config view by id")

    public ResponseEntity<ApiResponse<?>> getConfigViewById(@PathVariable(name = "id") Long id) {
        ConfigViewResponseDTO configView = configViewService.getConfigViewById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.get-by-id.success"), configView);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/name/{name}")
    @Operation(summary = "get data of config view by name", description = "Send a request via this API to get data of config view by name")

    public ResponseEntity<ApiResponse<?>> getConfigViewByName(@PathVariable(name = "name") String name) {
        ConfigViewResponseDTO configView = configViewService.getConfigViewByName(name);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.get-by-name.success"), configView);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/role")
    public ResponseEntity<ApiResponse<?>> getConfigViewByRoleCode(@RequestParam(name = "roleCodes") List<String> roleCodes) {
        List<ConfigViewResponseDTO> configViewList = configViewService.getConfigViewByRoleCode(roleCodes);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.get-by-role-id.success"), configViewList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/status/{status}")
    @Operation(summary = "get data of config view by status", description = "Send a request via this API to get data of config view by status")
    public ResponseEntity<ApiResponse<?>> getConfigViewByStatus(@PathVariable(name = "status") String status) {
        List<ConfigViewResponseDTO> configViewList = configViewService.getConfigViewByStatus(status);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.get-by-status.success"), configViewList);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(path = "/")
    @Operation(summary = "create and add a config view ", description = "Send a request via this API to create a new config view")
    public ResponseEntity<ApiResponse<?>> createConfigView(@RequestBody CreateConfigViewRequest configView) {
        ConfigViewResponseDTO configView1 = configViewService.createConfigView(configView);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("config-view.create.success"), configView1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update config view by id", description = "Send a request via this API to update config view by id")
    public ResponseEntity<ApiResponse<?>> updateConfigView(@PathVariable(name = "id") Long id, @RequestBody UpdateConfigViewRequest configView) {
        ConfigViewResponseDTO configView1 = configViewService.updateConfigView(id, configView);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.update.success"), configView1);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping(path = "/{id}/status")
    @Operation(summary = "update config view status by id", description = "Send a request via this API to update config view status by id")
    public ResponseEntity<ApiResponse<?>> updateConfigViewStatus(@PathVariable(name = "id") Long id, @RequestBody String status) {
        ConfigViewResponseDTO configView1 = configViewService.updateConfigViewStatus(id, status);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.update.success"), configView1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete config view by id", description = "Send a request via this API to delete config view by id")
    public ResponseEntity<ApiResponse<?>> deleteConfigView(@PathVariable(name = "id") Long id) {
        configViewService.deleteConfigView(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("config-view.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }


}
