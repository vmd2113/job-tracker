package com.duongw.commonservice.model.dto.response.category;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class CategoryResponseDTO {
    private Long categoryId;

    private String categoryCode;

    private String categoryName;

    private Long status;
}
