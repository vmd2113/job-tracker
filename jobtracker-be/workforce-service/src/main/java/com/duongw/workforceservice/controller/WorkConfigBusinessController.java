package com.duongw.workforceservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.workconfig.WorkConfigResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.workconfig.CreateWorkConfigRequest;
import com.duongw.workforceservice.model.dto.resquest.workconfig.UpdateWorkConfigRequest;
import com.duongw.workforceservice.service.IWorkConfigBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_WORK_CONFIG_BUSINESS)
@Slf4j
public class WorkConfigBusinessController {
    private final IWorkConfigBusinessService workConfigBusinessService;

    public WorkConfigBusinessController(IWorkConfigBusinessService workConfigBusinessService) {
        this.workConfigBusinessService = workConfigBusinessService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> getWorkConfigById(@PathVariable(name = "id") Long id) {
        WorkConfigResponseDTO workConfig = workConfigBusinessService.getWorkConfigById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-config.get-by-id.success"), workConfig);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> getAllWorkConfig() {
        List<WorkConfigResponseDTO> workConfig = workConfigBusinessService.getAllWorkConfig();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-config.get-all.success"), workConfig);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> createWorkConfig(@RequestBody CreateWorkConfigRequest workConfig) {
        WorkConfigResponseDTO workConfig1 = workConfigBusinessService.createWorkConfig(workConfig);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("work-config.create.success"), workConfig1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> updateWorkConfig(@RequestBody UpdateWorkConfigRequest workConfig, @PathVariable(name = "id") Long id) {
        WorkConfigResponseDTO workConfig1 = workConfigBusinessService.updateWorkConfig(id, workConfig);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-config.update.success"), workConfig1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteWorkConfigById(@PathVariable(name = "id") Long id) {
        workConfigBusinessService.deleteWorkConfigById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work-config.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping(path = "/delete/list")
    public ResponseEntity<ApiResponse<?>> deleteListWorkConfig(@RequestBody List<Long> ids) {
        workConfigBusinessService.deleteListWorkConfig(ids);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work-config.delete-list.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<?>> searchWorkConfig(
            @RequestParam(required = false, name = "workTypeName") String workTypeName,
            @RequestParam(required = false, name = "priorityId") Long priorityId,
            @RequestParam(required = false, name = "oldStatus") Long oldStatus,
            @RequestParam(required = false, name = "newStatus") Long newStatus,
            @RequestParam(defaultValue = "1", name = "page") int pageNo,
            @RequestParam(defaultValue = "10", name = "size") int pageSize,
            @RequestParam(defaultValue = "updateDate", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection) {
        log.info("WORK_CONFIG_BUSINESS_CONTROLLER  -> searchWorkConfig");
        PageResponse<WorkConfigResponseDTO> workConfigResponseDTOPageResponse = workConfigBusinessService.searchWorkConfig(workTypeName, priorityId, oldStatus, newStatus, pageNo, pageSize, sortBy, sortDirection);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-config.search.success"), workConfigResponseDTOPageResponse);
        return ResponseEntity.ok(apiResponse);
    }

}
