package com.duongw.common.model.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class BaseSpecification<T> implements Specification<T> {


    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(@NotNull Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (searchCriteria.getOperator().equals(">")) {
            return criteriaBuilder.greaterThan(root.get(searchCriteria.getKeyword()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperator().equals("<")) {
            return criteriaBuilder.lessThan(root.get(searchCriteria.getKeyword()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperator().equals(">=")) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(searchCriteria.getKeyword()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperator().equals("<=")) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(searchCriteria.getKeyword()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperator().equals("=")) {
            return criteriaBuilder.equal(root.get(searchCriteria.getKeyword()), searchCriteria.getValue());
        } else if (searchCriteria.getOperator().equals("!=")) {
            return criteriaBuilder.notEqual(root.get(searchCriteria.getKeyword()), searchCriteria.getValue());
        } else if (searchCriteria.getOperator().equalsIgnoreCase(":")) {
            if (root.get(searchCriteria.getKeyword()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(searchCriteria.getKeyword())),
                        "%" + searchCriteria.getValue().toString().toLowerCase() + "%"
                );
            } else {
                return criteriaBuilder.equal(root.get(searchCriteria.getKeyword()), searchCriteria.getValue());
            }
        }
        return null;
    }
}
