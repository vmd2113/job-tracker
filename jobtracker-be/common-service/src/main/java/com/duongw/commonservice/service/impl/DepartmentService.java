package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.department.CreateDepartmentRequest;
import com.duongw.commonservice.model.dto.request.department.UpdateDepartmentRequest;
import com.duongw.commonservice.model.dto.response.department.DepartmentResponseDTO;
import com.duongw.commonservice.model.dto.response.department.DepartmentTreeResponseDTO;
import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.model.projection.DepartmentProjection;
import com.duongw.commonservice.repository.DepartmentRepository;
import com.duongw.commonservice.repository.search.DepartmentSearchRepository;
import com.duongw.commonservice.service.IDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentSearchRepository departmentSearchRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentSearchRepository departmentSearchRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentSearchRepository = departmentSearchRepository;
    }

    private DepartmentResponseDTO convertToDepartmentResponseDTO(Department department) {
        DepartmentResponseDTO departmentResponseDTO = new DepartmentResponseDTO();
        departmentResponseDTO.setDepartmentId(department.getDepartmentId());
        departmentResponseDTO.setDepartmentName(department.getDepartmentName());
        departmentResponseDTO.setDepartmentCode(department.getDepartmentCode());

        Department parentDepartment = department.getParentDepartment();

        if (parentDepartment != null) {
            departmentResponseDTO.setDepartmentParentName(parentDepartment.getDepartmentName());
            departmentResponseDTO.setDepartmentParentId(parentDepartment.getDepartmentId());

        }
        departmentResponseDTO.setStatus(department.getStatus());
        return departmentResponseDTO;
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartment() {
        log.info("");
        List<Department> departmentList = departmentRepository.findAll();
        return departmentList.stream().map(this::convertToDepartmentResponseDTO).toList();
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not.found")));
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
    public DepartmentResponseDTO createDepartment(CreateDepartmentRequest department) {
        Department newDepartment = new Department();
        newDepartment.setDepartmentName(department.getDepartmentName());
        newDepartment.setDepartmentCode(department.getDepartmentCode());

        if (department.getDepartmentParentId() == null) {
            newDepartment.setParentDepartment(null);

        } else {
            Department parentDepartment = departmentRepository.findById(department.getDepartmentParentId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not.found")));
            newDepartment.setParentDepartment(parentDepartment);

        }


        newDepartment.setStatus(department.getStatus());
        return convertToDepartmentResponseDTO(departmentRepository.save(newDepartment));
    }


    @Override
    public DepartmentResponseDTO updateDepartment(Long id, UpdateDepartmentRequest department) {
        Department updateDepartment = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not.found")));
        updateDepartment.setDepartmentName(department.getDepartmentName());
        updateDepartment.setDepartmentCode(department.getDepartmentCode());
        updateDepartment.setStatus(department.getStatus());

        if (department.getDepartmentParentId() == null) {
            updateDepartment.setParentDepartment(null);
        }
        Department parentDepartment = departmentRepository.findById(department.getDepartmentParentId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not.found")));
        updateDepartment.setParentDepartment(parentDepartment);

        Department save = departmentRepository.saveAndFlush(updateDepartment);
        return convertToDepartmentResponseDTO(save);

    }

    @Override
    public DepartmentResponseDTO updateDepartmentStatus(Long id, String status) {
        Department updateDepartment = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not.found")));
        //TODO: set status
        updateDepartment.setStatus(1L);
        return convertToDepartmentResponseDTO(departmentRepository.save(updateDepartment));
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("department.not.found")));
        departmentRepository.delete(department);

    }

    @Override
    public PageResponse<DepartmentResponseDTO> searchDepartments(String departmentName, String departmentCode, int pageNo, int pageSize, String sortBy, String sortDirection) {
        return departmentSearchRepository.searchDepartments(departmentName, departmentCode, pageNo, pageSize, sortBy, sortDirection);
    }


    // using connect by query
    @Override
    public List<DepartmentTreeResponseDTO> getDepartmentHierarchy() {
        List<DepartmentProjection> departments = departmentRepository.findDepartmentHierarchy();

        Map<Long, DepartmentTreeResponseDTO> dtoMap = new HashMap<>();

        for (DepartmentProjection dept : departments) {
            DepartmentTreeResponseDTO dto = convertToDTO(dept);
            dtoMap.put(dept.getId(), dto);
        }

        List<DepartmentTreeResponseDTO> rootDepartments = new ArrayList<>();

        for (DepartmentProjection dept : departments) {
            DepartmentTreeResponseDTO currentDto = dtoMap.get(dept.getId());

            if (dept.getParentId() == null) {
                // Đây là node gốc
                rootDepartments.add(currentDto);
            } else {
                // Thêm vào danh sách con của node cha
                DepartmentTreeResponseDTO parentDto = dtoMap.get(dept.getParentId());
                if (parentDto != null) {
                    if (parentDto.getChildren() == null) {
                        parentDto.setChildren(new ArrayList<>());
                    }
                    parentDto.getChildren().add(currentDto);
                }
            }
        }

        return rootDepartments;
    }

    @Override
    public List<DepartmentTreeResponseDTO> getListChildDepartmentByParentId(Long departmentId) {
        List<DepartmentProjection> children = departmentRepository.findDepartmentTreeByIdConnectBy(departmentId);
        // Vì truy vấn CONNECT BY trả về node gốc và các cấp con,
        // ta lọc lấy những bản ghi có depth = 2 (con trực tiếp)
        List<DepartmentProjection> directChildren = new ArrayList<>();
        for (DepartmentProjection proj : children) {
            if (proj.getDepth() != null && proj.getDepth() == 2) {
                directChildren.add(proj);
            }
        }
        return convertToTreeDTOList(directChildren);

    }

    private List<DepartmentTreeResponseDTO> convertToTreeDTOList(List<DepartmentProjection> projections) {
        List<DepartmentTreeResponseDTO> list = new ArrayList<>();
        for (DepartmentProjection proj : projections) {
            list.add(convertToDTO(proj));
        }
        return list;
    }






    private DepartmentTreeResponseDTO convertToDTO(DepartmentProjection dept) {
        DepartmentTreeResponseDTO dto = new DepartmentTreeResponseDTO();
        dto.setDepartmentId(dept.getId());
        dto.setDepartmentName(dept.getName());
        dto.setDepartmentCode(dept.getCode());
        dto.setStatus(dept.getStatus());

        // Thông tin về department cha
        if (dept.getParentId() != null) {
            dto.setDepartmentParentId(dept.getParentId());
            dto.setDepartmentParentName(dept.getParentName());
            dto.setDepartmentParentCode(dept.getParentCode());
        }

        return dto;
    }
}
