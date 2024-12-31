package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.commonservice.model.dto.request.department.CreateDepartmentRequest;
import com.duongw.commonservice.model.dto.request.department.UpdateDepartmentRequest;
import com.duongw.commonservice.model.dto.response.department.DepartmentResponseDTO;
import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.repository.DepartmentRepository;
import com.duongw.commonservice.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {


    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    private DepartmentResponseDTO convertToDepartmentResponseDTO(Department department) {
        DepartmentResponseDTO departmentResponseDTO = new DepartmentResponseDTO();
        departmentResponseDTO.setDepartmentId(department.getDepartmentId());
        departmentResponseDTO.setDepartmentName(department.getDepartmentName());
        departmentResponseDTO.setDepartmentCode(department.getDepartmentCode());
        departmentResponseDTO.setDepartmentParentId(department.getDepartmentParentId());
        departmentResponseDTO.setStatus(department.getStatus());
        return departmentResponseDTO;
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartment() {
        List<Department> departmentList = departmentRepository.findAll();
        return departmentList.stream().map(this::convertToDepartmentResponseDTO).toList();
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("department.not.found")));
        return convertToDepartmentResponseDTO(department);
    }

    @Override
    public DepartmentResponseDTO getDepartmentByName(String name) {
        Department department = departmentRepository.findByDepartmentName(name);
        return convertToDepartmentResponseDTO(department);
    }

    @Override
    public DepartmentResponseDTO getDepartmentByCode(String code) {
        Department department = departmentRepository.findByDepartmentCode(code);
        return convertToDepartmentResponseDTO(department);
    }

    @Override
    public List<DepartmentResponseDTO> getDepartmentByParentId(Long parentId) {
        List<Department> departmentList = departmentRepository.findByDepartmentParentId(parentId);
        return departmentList.stream().map(this::convertToDepartmentResponseDTO).toList();
    }

    @Override
    public DepartmentResponseDTO createDepartment(CreateDepartmentRequest department) {
        Department newDepartment = new Department();
        newDepartment.setDepartmentName(department.getDepartmentName());
        newDepartment.setDepartmentCode(department.getDepartmentCode());
        newDepartment.setDepartmentParentId(department.getDepartmentParentId());
        newDepartment.setStatus(department.getStatus());
        return convertToDepartmentResponseDTO(departmentRepository.save(newDepartment));
    }



    @Override
    public DepartmentResponseDTO updateDepartment(Long id, UpdateDepartmentRequest department) {
        Department updateDepartment = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("department.not.found")));
        updateDepartment.setDepartmentName(department.getDepartmentName());
        updateDepartment.setDepartmentCode(department.getDepartmentCode());
        updateDepartment.setDepartmentParentId(department.getDepartmentParentId());
        updateDepartment.setStatus(department.getStatus());
        return convertToDepartmentResponseDTO(departmentRepository.save(updateDepartment));
    }

    @Override
    public DepartmentResponseDTO updateDepartmentStatus(Long id, String status) {
        Department updateDepartment = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("department.not.found")));
        //TODO: set status
        updateDepartment.setStatus(1L);
        return convertToDepartmentResponseDTO(departmentRepository.save(updateDepartment));
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("department.not.found")));
        departmentRepository.delete(department);

    }
}
