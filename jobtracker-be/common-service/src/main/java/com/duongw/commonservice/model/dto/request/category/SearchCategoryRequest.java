package com.duongw.commonservice.model.dto.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCategoryRequest {
    private String categoryCode;
    private String categoryName;
}
