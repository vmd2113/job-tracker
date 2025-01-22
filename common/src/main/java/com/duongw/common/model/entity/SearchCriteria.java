package com.duongw.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SearchCriteria {
    private String keyword;
    private String operator;
    private Object value;
}
