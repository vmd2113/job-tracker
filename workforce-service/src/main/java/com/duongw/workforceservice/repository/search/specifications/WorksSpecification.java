package com.duongw.workforceservice.repository.search.specifications;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.workforceservice.model.entity.Works;
import org.springframework.data.jpa.domain.Specification;

public class WorksSpecification extends BaseSpecification<Works> {

    public WorksSpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

    public static Specification<Works> searchByMultipleFields(String workCode, String workContent, Long workTypeId,
                                                              Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId) {

        Specification<Works> specification = Specification.where(null);

        // Text fields với like operator
        if (workCode != null && !workCode.trim().isEmpty()) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("workCode", ":", workCode.trim())));
        }

        if (workContent != null && !workContent.trim().isEmpty()) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("workContent", ":", workContent.trim())));
        }

        // Exact match fields
        if (workTypeId != null) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("workType.workTypeId", "=", workTypeId)));
        }

        if (priorityId != null) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("priorityId", "=", priorityId)));
        }

        if (status != null) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("status", "=", status)));
        }

        if (assignedUserId != null) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("assignedUserId", "=", assignedUserId)));
        }

        // DateTime fields - BaseSpecification sẽ tự động xử lý việc parse datetime
        if (startTime != null && !startTime.trim().isEmpty()) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("startTime", ">=", startTime.trim())));
        }

        if (endTime != null && !endTime.trim().isEmpty()) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("endTime", "<=", endTime.trim())));
        }

        if (finishTime != null && !finishTime.trim().isEmpty()) {
            specification = specification.and(new WorksSpecification(
                    new SearchCriteria("finishTime", ">=", finishTime.trim())));
        }

        return specification;
    }
}