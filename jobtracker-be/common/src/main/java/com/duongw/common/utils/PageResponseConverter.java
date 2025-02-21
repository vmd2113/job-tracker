package com.duongw.common.utils;

import com.duongw.common.model.dto.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public class PageResponseConverter {

    public static <T> PageResponse<T> convertFromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .total(page.getTotalElements())
                .items(page.getContent())
                .pageNo(page.getNumber()+1)
                .pageSize(page.getSize())
                .build();
    }

    // Nếu bạn muốn chuyển từ danh sách đơn thuần (không có phân trang)
    public static <T> PageResponse<T> convertFromList(List<T> list, long total) {
        return PageResponse.<T>builder()
                .total(total)
                .items(list)
                .pageNo(1)          // Default, có thể tính lại tùy theo yêu cầu
                .pageSize(list.size())
                .build();
    }
}
