package com.duongw.workforceservice.repository.search.specifications;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import com.duongw.workforceservice.model.entity.WorkType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WorkConfigSpecification {

    public static Specification<WorkConfigBusiness> searchByMultipleFields(
            String workTypeName,
            Long priorityId,
            Long oldStatus,
            Long newStatus) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join với WorkType
            Join<WorkConfigBusiness, WorkType> workTypeJoin = root.join("workType", JoinType.INNER);

            // Thêm điều kiện tìm kiếm theo workTypeName
            if (workTypeName != null && !workTypeName.trim().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(workTypeJoin.get("workTypeName")),
                        "%" + workTypeName.toLowerCase() + "%"
                ));
            }

            // Thêm điều kiện tìm kiếm theo priorityId
            if (priorityId != null) {
                predicates.add(cb.equal(root.get("priorityId"), priorityId));
            }

            // Thêm điều kiện tìm kiếm theo oldStatus
            if (oldStatus != null) {
                predicates.add(cb.equal(root.get("oldStatus"), oldStatus));
            }

            // Thêm điều kiện tìm kiếm theo newStatus
            if (newStatus != null) {
                predicates.add(cb.equal(root.get("newStatus"), newStatus));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
