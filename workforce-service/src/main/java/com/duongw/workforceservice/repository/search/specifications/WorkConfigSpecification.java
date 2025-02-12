package com.duongw.workforceservice.repository.search.specifications;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import org.springframework.data.jpa.domain.Specification;

public class WorkConfigSpecification extends BaseSpecification<WorkConfigBusiness> {

    public WorkConfigSpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

    public static Specification<WorkConfigBusiness> searchByMultipleFields(String workTypeName, Long priorityId, Long oldStatus, Long newStatus) {

        Specification<WorkConfigBusiness> workConfigSpecification = Specification.where(null);

        // default search with like operator


        if (workTypeName != null && !workTypeName.isEmpty())
            workConfigSpecification = workConfigSpecification.and(new WorkConfigSpecification(new SearchCriteria(workTypeName, ":", workTypeName)));
        if (priorityId != null)
            workConfigSpecification = workConfigSpecification.and(new WorkConfigSpecification(new SearchCriteria("priorityId", "=", priorityId)));
        if (oldStatus != null)
            workConfigSpecification = workConfigSpecification.and(new WorkConfigSpecification(new SearchCriteria("oldStatus", "=", oldStatus)));
        if (newStatus != null)
            workConfigSpecification = workConfigSpecification.and(new WorkConfigSpecification(new SearchCriteria("newStatus", "=", newStatus)));
        return workConfigSpecification;
    }
}
