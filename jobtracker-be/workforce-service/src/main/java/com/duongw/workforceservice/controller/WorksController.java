package com.duongw.workforceservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.works.CreateWorkRequest;
import com.duongw.workforceservice.model.dto.resquest.works.UpdateWorkRequest;
import com.duongw.workforceservice.service.IWorkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_WORKS)
public class WorksController {


    private final IWorkService workService;

    public WorksController(IWorkService workService) {
        this.workService = workService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> getAllWork() {

        List<WorkResponseDTO> workList = workService.getAllWork();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work.get-all.success"), workList);
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping(path = "/{id}")

    public ResponseEntity<ApiResponse<?>> getWorkById(@PathVariable(name = "id") Long id) {

        WorkResponseDTO work = workService.getWorkById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work.get-by-id.success"), work);
        return ResponseEntity.ok(apiResponse);

    }

    @PostMapping(path = "/")

    public ResponseEntity<ApiResponse<?>> createWork(@RequestBody CreateWorkRequest work) {
        WorkResponseDTO work1 = workService.createWork(work);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("work.create.success"), work1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")

    public ResponseEntity<ApiResponse<?>> updateWork(@RequestBody UpdateWorkRequest work, @PathVariable(name = "id") Long id) {
        WorkResponseDTO work1 = workService.updateWork(id, work);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work.update.success"), work1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteWorkById(@PathVariable(name = "id") Long id) {
        workService.deleteWorkById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/delete/list")
    public ResponseEntity<ApiResponse<?>> deleteListWorks(@RequestBody List<Long> ids) {
        workService.deleteListWorks(ids);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work.delete-list.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<?>> searchWorks(
            @RequestParam(name = "workCode", required = false) String workCode,
            @RequestParam(name = "workContent", required = false) String workContent,
            @RequestParam(name = "workTypeId", required = false) Long workTypeId,
            @RequestParam(name = "priorityId", required = false) Long priorityId,
            @RequestParam(name = "status", required = false) Long status,
            @RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "endTime", required = false) String endTime,
            @RequestParam(name = "assignedUserId", required = false) Long assignedUserId,
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = "updateDate") String sortBy,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {
        PageResponse<WorkResponseDTO> workList = workService.searchWorks(workCode, workContent, workTypeId, priorityId, status, startTime, endTime, assignedUserId, pageNo, pageSize, sortBy, sortDirection);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work.search.success"), workList);
        return ResponseEntity.ok(apiResponse);
    }
}
