package com.duongw.workforceservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.worktype.CreateWorkTypeRequest;
import com.duongw.workforceservice.model.dto.resquest.worktype.UpdateWorkTypeRequest;
import com.duongw.workforceservice.service.IWorkTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_WORK_TYPE)
@Slf4j

public class WorkTypeController {

    private final IWorkTypeService workTypeService;

    public WorkTypeController(IWorkTypeService workTypeService) {
        this.workTypeService = workTypeService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> getAllWorkType() {
        log.info("WORK_TYPE_CONTROLLER  -> getAllWorkType");
        List<WorkTypeResponseDTO> workTypeList = workTypeService.getAllWorkType();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-type.get-all.success"), workTypeList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ApiResponse<?>> getWorkTypeById(@PathVariable(name = "id") Long id) {
        log.info("WORK_TYPE_CONTROLLER  -> getWorkTypeById");
        WorkTypeResponseDTO workType = workTypeService.getWorkTypeById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-type.get.success"), workType);
        return ResponseEntity.ok(apiResponse);


    }

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> createWorkType(@RequestBody CreateWorkTypeRequest workType) {
        log.info("WORK_TYPE_CONTROLLER  -> createWorkType");
        WorkTypeResponseDTO workType1 = workTypeService.createWorkType(workType);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("work-type.create.success"), workType1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> updateWorkType(@PathVariable(name = "id") long id, @RequestBody UpdateWorkTypeRequest workType) {
        log.info("WORK_TYPE_CONTROLLER  -> updateWorkType");
        WorkTypeResponseDTO workType1 = workTypeService.updateWorkType(id, workType);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-type.update.success"), workType1);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping(path = "/{id}/status")
    public ResponseEntity<ApiResponse<?>> changeStatusWorkType(@PathVariable(name = "id") long id, String status) {
        log.info("WORK_TYPE_CONTROLLER  -> changeStatusWorkType");
        WorkTypeResponseDTO workType1 = workTypeService.changeStatusWorkType(id, status);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-type.change-status.success"), workType1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteWorkTypeById(@PathVariable(name = "id") long id) {
        log.info("WORK_TYPE_CONTROLLER  -> deleteWorkTypeById");
        workTypeService.deleteWorkTypeById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work-type.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/delete/list")
    public ResponseEntity<ApiResponse<?>> deleteListWorkType(@RequestBody List<Long> ids) {
        log.info("WORK_TYPE_CONTROLLER  -> deleteListWorkType");
        workTypeService.deleteListWorkTypes(ids);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work-type.delete-list.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<?>> searchWorkType(
            @RequestParam(name  = "workTypeCode", required = false) String workTypeCode,
            @RequestParam(name  = "workTypeName", required = false) String workTypeName,
            @RequestParam(name = "page",defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy",defaultValue = "updateDate") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection) {
        log.info("WORK_TYPE_CONTROLLER  -> searchWorkType");
        PageResponse<WorkTypeResponseDTO> workTypeResponseDTOPageResponse = workTypeService.searchWorkType(workTypeCode, workTypeName, pageNo, pageSize, sortBy, sortDirection);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-type.search.success"), workTypeResponseDTOPageResponse);
        return ResponseEntity.ok(apiResponse);
    }
}
