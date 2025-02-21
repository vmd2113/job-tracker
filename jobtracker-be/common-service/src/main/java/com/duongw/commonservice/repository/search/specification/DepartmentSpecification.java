package com.duongw.commonservice.repository.search.specification;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.commonservice.model.entity.Department;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


public class DepartmentSpecification extends BaseSpecification<Department> {


    public DepartmentSpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

    public static Specification<Department> searchByMultipleFields(String departmentName, String departmentCode) {
        Specification<Department> spec = Specification.where(null);

        if (departmentName != null && !departmentName.isEmpty()) {
            spec = spec.and(new DepartmentSpecification(
                    new SearchCriteria("departmentName", ":", departmentName)));
        }

        if (departmentCode != null && !departmentCode.isEmpty()) {
            spec = spec.and(new DepartmentSpecification(
                    new SearchCriteria("departmentCode", ":", departmentCode)));
        }

        return spec;
    }

}
