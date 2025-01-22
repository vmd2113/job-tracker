package com.duongw.commonservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.commonservice.model.dto.response.department.DepartmentResponseDTO;
import com.duongw.commonservice.model.dto.response.department.DepartmentTreeResponseDTO;
import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.repository.DepartmentRepository;
import com.duongw.commonservice.repository.search.specification.DepartmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentSearchRepository {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentSearchRepository(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    public PageResponse<DepartmentResponseDTO> searchDepartments(
            String departmentName,
            String departmentCode,
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        int pageAdjust = pageNo > 1 ? pageNo - 1 : 0;

        PageRequest pageRequest = PageRequest.of(pageAdjust, pageSize, sort);

        Page<Department> departmentPage = departmentRepository.findAll(
                DepartmentSpecification.searchByMultipleFields(departmentName, departmentCode),
                pageRequest
        );



        Page<DepartmentResponseDTO> departmentResponsePage = departmentPage.map(department -> DepartmentResponseDTO.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .departmentCode(department.getDepartmentCode())
                .departmentParentId(department.getParentDepartment() == null ? null : department.getParentDepartment().getDepartmentId())
                .status(department.getStatus())
                .build());

        return PageResponseConverter.convertFromPage(departmentResponsePage);
    }


}
