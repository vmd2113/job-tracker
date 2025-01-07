package com.duongw.common.utils;

import com.duongw.common.model.dto.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public class PageResponseConverter {

    public static <T> PageResponse<T> convertFromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .total(page.getTotalElements())  // Tổng số phần tử
                .items(page.getContent())        // Danh sách phần tử trong trang
                .pageNo(page.getNumber())        // Số trang hiện tại (0-indexed)
                .pageSize(page.getSize())        // Kích thước trang
                .build();
    }

    // Nếu bạn muốn chuyển từ danh sách đơn thuần (không có phân trang)
    public static <T> PageResponse<T> convertFromList(List<T> list, long total) {
        return PageResponse.<T>builder()
                .total(total)
                .items(list)
                .pageNo(0)          // Default, có thể tính lại tùy theo yêu cầu
                .pageSize(list.size())
                .build();
    }
}
