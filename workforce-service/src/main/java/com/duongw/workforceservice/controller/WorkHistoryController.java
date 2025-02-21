package com.duongw.workforceservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.workforceservice.model.dto.response.workhistory.WorkHistoryResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.workhistory.CreateWorkHistory;
import com.duongw.workforceservice.model.dto.resquest.workhistory.UpdateWorkHistory;
import com.duongw.workforceservice.service.IWorkHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_WORK_HISTORY)
@Slf4j

public class WorkHistoryController {

    private final IWorkHistoryService workHistoryService;

    public WorkHistoryController(IWorkHistoryService workHistoryService) {
        this.workHistoryService = workHistoryService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> getAllWorkHistory() {
        List<WorkHistoryResponseDTO> workHistoryList = workHistoryService.getAllWorkHistory();

        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-history.get-all.success"), workHistoryList);
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> getWorkHistoryById(@PathVariable(name = "id") Long id) {
        WorkHistoryResponseDTO workHistory = workHistoryService.getWorkHistoryById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-history.get-by-id.success"), workHistory);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse<?>> createWorkHistory(@RequestBody CreateWorkHistory workHistory) {
        WorkHistoryResponseDTO workHistory1 = workHistoryService.createWorkHistory(workHistory);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("work-history.create.success"), workHistory1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> updateWorkHistory(@RequestBody UpdateWorkHistory workHistory, @PathVariable(name = "id") Long id) {
        WorkHistoryResponseDTO workHistory1 = workHistoryService.updateWorkHistory(id, workHistory);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-history.update.success"), workHistory1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteWorkHistoryById(@PathVariable(name = "id") Long id) {
        workHistoryService.deleteWorkHistoryById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work-history.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/delete/list")
    public ResponseEntity<ApiResponse<?>> deleteListWorkHistory(@RequestBody List<Long> ids) {
        workHistoryService.deleteListWorkHistory(ids);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("work-history.delete-list.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<?>> searchWorkHistory(@RequestParam(name = "page", defaultValue = "1") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size,

                                                            @RequestParam(name = "workCode", required = false) String workCode,
                                                            @RequestParam(name = "workContent", required = false) String workContent,
                                                            @RequestParam(name = "workTypeId", required = false) Long workTypeId,
                                                            @RequestParam(name = "priorityId", required = false) Long priorityId,
                                                            @RequestParam(name = "status", required = false) Long status,
                                                            @RequestParam(name = "startTime", required = false) String startTime,
                                                            @RequestParam(name = "endTime", required = false) String endTime,
                                                            @RequestParam(name = "finishTime", required = false) String finishTime,
                                                            @RequestParam(name = "assignedUserId", required = false) Long assignedUserId,
                                                            @RequestParam(name = "sortBy", required = false, defaultValue = "updateDate") String sortBy,
                                                            @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {
        List<WorkHistoryResponseDTO> workHistoryList = workHistoryService.searchWorkHistory(page, size, workCode, workContent, workTypeId, priorityId, status, startTime, endTime, finishTime, assignedUserId, sortBy, sortDirection);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("work-history.search.success"), workHistoryList);
        return ResponseEntity.ok(apiResponse);
    }


}
