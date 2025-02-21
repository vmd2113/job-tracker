package com.duongw.commonservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {


    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO getCategoryByName(String name);

    CategoryResponseDTO getCategoryByCode(String code);

    List<CategoryResponseDTO> getAllCategory();

    CategoryResponseDTO createCategory(CreateCategoryRequest category);

    CategoryResponseDTO updateCategory(Long id, UpdateCategoryRequest category);

    void deleteCategory(Long id);

    void deleteCategoryList(List<Long> ids);

    PageResponse<CategoryResponseDTO> searchCategories(String code, String name, int pageNo, int pageSize, String sortBy, String sortDirect);
}
