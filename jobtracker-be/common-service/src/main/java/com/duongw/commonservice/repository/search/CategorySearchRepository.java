package com.duongw.commonservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;
import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.repository.CategoryRepository;
import com.duongw.commonservice.repository.search.specification.CategorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class CategorySearchRepository {


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategorySearchRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public PageResponse<CategoryResponseDTO> searchCategory(
            String categoryCode,
            String categoryName,
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDirect
    ) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirect), sortBy);

        int pageAdjust = pageNo > 1 ? pageNo - 1 : 0;

        PageRequest pageRequest = PageRequest.of(pageAdjust, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(CategorySpecification.searchByMultipleFields(categoryName, categoryCode), pageRequest);

        Page<CategoryResponseDTO> categoryResponseDTOPage = categoryPage.map(category -> CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryCode(category.getCategoryCode())
                .categoryName(category.getCategoryName())
                .status(category.getStatus())
                .build());
        return PageResponseConverter.convertFromPage(categoryResponseDTOPage);
    }


}
