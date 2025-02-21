package com.duongw.commonservice.model.dto.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdateItemRequest {

    private String itemName;

    private String itemCode;

    private String itemValue;

    private Long parentItemId;

    private Long categoryId;

    private Long status;
}
