package com.duongw.common.model.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public abstract class AbstractEntityCriteriaQuery<T> {

    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;

    public AbstractEntityCriteriaQuery(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    protected abstract Class<T> getEntityClass();

    protected CriteriaQuery<T> createQuery() {
        return criteriaBuilder.createQuery(getEntityClass());
    }

    protected CriteriaQuery<Long> createCountQuery() {
        return criteriaBuilder.createQuery(Long.class);
    }


}
