package com.duongw.common.model.entity;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
public class BaseSpecification<T> implements Specification<T> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(@NotNull Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Xử lý nested path
        Path<?> path = root;
        String keyword = searchCriteria.getKeyword();

        if (keyword.contains(".")) {
            String[] pathParts = keyword.split("\\.");
            for (String part : pathParts) {
                path = path.get(part);
            }
        } else {
            path = root.get(keyword);
        }

        // Kiểm tra kiểu dữ liệu của path
        Class<?> pathType = path.getJavaType();
        Object value = parseValue(pathType, searchCriteria.getValue());

        // Xử lý các operators
        return createPredicate(path, pathType, criteriaBuilder, value);
    }

    private Object parseValue(Class<?> targetType, Object value) {
        if (value == null) return null;

        // Xử lý đặc biệt cho LocalDateTime
        if (targetType == LocalDateTime.class && value instanceof String) {
            try {
                return LocalDateTime.parse((String) value, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid datetime format for value: " + value
                        + ". Expected format: yyyy-MM-dd'T'HH:mm:ss", e);
            }
        }

        // Xử lý các kiểu dữ liệu khác nếu cần
        if (targetType == Long.class && value instanceof String) {
            return Long.parseLong((String) value);
        }
        if (targetType == Integer.class && value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (targetType == Double.class && value instanceof String) {
            return Double.parseDouble((String) value);
        }

        return value;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate createPredicate(Path<?> path, Class<?> pathType, CriteriaBuilder criteriaBuilder, Object value) {
        String operator = searchCriteria.getOperator();

        // Xử lý operator like cho String
        if (operator.equalsIgnoreCase(":")) {
            if (pathType == String.class) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(path.as(String.class)),
                        "%" + value.toString().toLowerCase() + "%"
                );
            } else {
                return criteriaBuilder.equal(path, value);
            }
        }

        // Xử lý các operators so sánh
        if (Comparable.class.isAssignableFrom(pathType)) {
            Path<Comparable> comparablePath = (Path<Comparable>) path;
            Comparable comparableValue = (Comparable) value;

            switch (operator) {
                case ">" -> {
                    return criteriaBuilder.greaterThan(comparablePath, comparableValue);
                }
                case "<" -> {
                    return criteriaBuilder.lessThan(comparablePath, comparableValue);
                }
                case ">=" -> {
                    return criteriaBuilder.greaterThanOrEqualTo(comparablePath, comparableValue);
                }
                case "<=" -> {
                    return criteriaBuilder.lessThanOrEqualTo(comparablePath, comparableValue);
                }
                case "=" -> {
                    return criteriaBuilder.equal(path, value);
                }
                case "!=" -> {
                    return criteriaBuilder.notEqual(path, value);
                }
            }
        }

        // Xử lý equality cho các kiểu dữ liệu không phải Comparable
        if (operator.equals("=")) {
            return criteriaBuilder.equal(path, value);
        } else if (operator.equals("!=")) {
            return criteriaBuilder.notEqual(path, value);
        }

        return null;
    }
}