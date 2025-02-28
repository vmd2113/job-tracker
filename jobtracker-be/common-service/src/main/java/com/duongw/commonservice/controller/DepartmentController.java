package com.duongw.commonservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.department.CreateDepartmentRequest;
import com.duongw.commonservice.model.dto.request.department.UpdateDepartmentRequest;
import com.duongw.commonservice.model.dto.response.department.DepartmentResponseDTO;
import com.duongw.commonservice.model.dto.response.department.DepartmentTreeResponseDTO;
import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.service.IDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_DEPARTMENT)
@Slf4j
public class DepartmentController {

    private final IDepartmentService departmentService;

    @Autowired
    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(path = "/")
    @Operation(summary = "get all data of departments", description = "Send a request via this API to get all data of departments")

    public ResponseEntity<ApiResponse<?>> getAllDepartment() {
        log.info("DEPARTMENT_CONTROLLER -> getAllDepartment");
        List<DepartmentResponseDTO> departmentList = departmentService.getAllDepartment();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.get-all.success"), departmentList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "get data of department by id", description = "Send a request via this API to get data of department by id")

    public ResponseEntity<ApiResponse<?>> getDepartmentById(@PathVariable(name = "id") Long id) {
        log.info("DEPARTMENT_CONTROLLER -> getDepartmentById");
        DepartmentResponseDTO department = departmentService.getDepartmentById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.get-by-id.success"), department);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/name/{name}")
    @Operation(summary = "get data of department by name", description = "Send a request via this API to get data of department by name")
    public ResponseEntity<ApiResponse<?>> getDepartmentByName(@PathVariable(name = "name") String name) {
        log.info("DEPARTMENT_CONTROLLER -> getDepartmentByName");
        DepartmentResponseDTO department = departmentService.getDepartmentByName(name);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.get-by-name.success"), department);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/code/{code}")
    @Operation(summary = "get data of department by code", description = "Send a request via this API to get data of department by code")
    public ResponseEntity<ApiResponse<?>> getDepartmentByCode(@PathVariable(name = "code") String code) {
        log.info("DEPARTMENT_CONTROLLER -> getDepartmentByCode");
        DepartmentResponseDTO department = departmentService.getDepartmentByCode(code);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.get-by-code.success"), department);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/search")
    @Operation(summary = "search department by criteria", description = "Send a request via this API to search department by criteria")
    public ResponseEntity<ApiResponse<?>> searchDepartmentByCriteria(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                                                     @RequestParam(name = "departmentName", required = false) String departmentName,
                                                                     @RequestParam(name = "departmentCode", required = false) String departmentCode,
                                                                     @RequestParam(name = "sortBy", required = false, defaultValue = "updateDate") String sortBy,
                                                                     @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {
        log.info("DEPARTMENT_CONTROLLER -> searchDepartmentByCriteria");
        PageResponse<DepartmentResponseDTO> pageResponse = departmentService.searchDepartments(departmentName, departmentCode, page, size, sortBy, sortDirection);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.search.success"), pageResponse);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping(path = "/hierarchy")
    @Operation(summary = "get department hierarchy", description = "Send a request via this API to get department hierarchy")
    public ResponseEntity<ApiResponse<?>> getDepartmentHierarchy() {
        log.info("DEPARTMENT_CONTROLLER -> getDepartmentHierarchy");
        List<DepartmentTreeResponseDTO> departmentTreeResponseDTOList = departmentService.getDepartmentHierarchy();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.get-hierarchy.success"), departmentTreeResponseDTOList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/hierarchy/parent/{id}")
    @Operation(summary = "get direct children of a department", description = "Send a request via this API to get direct children of a department")
    public ResponseEntity<ApiResponse<?>> getListChildDepartmentByParentId(@PathVariable(name = "id") Long id) {
        log.info("DEPARTMENT_CONTROLLER -> getListChildDepartmentByParentId for parent ID: {}", id);
        List<DepartmentTreeResponseDTO> departmentTreeResponseDTOList = departmentService.getListChildDepartmentByParentId(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("department.get-hierarchy.success"), departmentTreeResponseDTOList);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(path = "/")
    @Operation(summary = "create and add a department ", description = "Send a request via this API to create a new department")
    public ResponseEntity<ApiResponse<?>> createDepartment(@RequestBody CreateDepartmentRequest department) {
        log.info("DEPARTMENT_CONTROLLER -> createDepartment");
        DepartmentResponseDTO department1 = departmentService.createDepartment(department);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("department.create.success"), department1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update department by id", description = "Send a request via this API to update department by id")
    public ResponseEntity<ApiResponse<?>> updateDepartment(@PathVariable(name = "id") Long id, @RequestBody UpdateDepartmentRequest department) {
        log.info("DEPARTMENT_CONTROLLER -> updateDepartment");
        DepartmentResponseDTO department1 = departmentService.updateDepartment(id, department);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "update department success", department1);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping(path = "/{id}/status")
    @Operation(summary = "update department status by id", description = "Send a request via this API to update department status by id")
    public ResponseEntity<ApiResponse<?>> updateDepartmentStatus(@PathVariable(name = "id") Long id, @RequestBody String status) {
        DepartmentResponseDTO department1 = departmentService.updateDepartmentStatus(id, status);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "update department success", department1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete department by id", description = "Send a request via this API to delete department by id")
    public ResponseEntity<ApiResponse<?>> deleteDepartment(@PathVariable(name = "id") Long id) {
        departmentService.deleteDepartment(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "delete department success");
        return ResponseEntity.ok(apiResponse);
    }


}
