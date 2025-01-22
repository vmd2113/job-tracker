package com.duongw.commonservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.department.CreateDepartmentRequest;
import com.duongw.commonservice.model.dto.request.department.UpdateDepartmentRequest;
import com.duongw.commonservice.model.dto.response.department.DepartmentResponseDTO;
import com.duongw.commonservice.model.dto.response.department.DepartmentTreeResponseDTO;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentResponseDTO> getAllDepartment();

    DepartmentResponseDTO getDepartmentById(Long id);

    DepartmentResponseDTO getDepartmentByName(String name);

    DepartmentResponseDTO getDepartmentByCode(String code);


    DepartmentResponseDTO createDepartment(CreateDepartmentRequest department);

    DepartmentResponseDTO updateDepartment(Long id, UpdateDepartmentRequest department);

    DepartmentResponseDTO updateDepartmentStatus(Long id, String status);
    void deleteDepartment(Long id);

    PageResponse<DepartmentResponseDTO> searchDepartments(String departmentName, String departmentCode, int pageNo, int pageSize, String sortBy, String sortDirection);

    List<DepartmentTreeResponseDTO> getDepartmentHierarchy();
}
