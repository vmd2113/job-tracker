package com.duongw.common.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PageResponse<T> implements Serializable {

    private Long total;
    private List<T> items;
    private int pageNo ;
    private int pageSize;





}
