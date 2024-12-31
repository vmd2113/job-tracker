package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;

import java.util.List;

public interface ICategoryService {



    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO getCategoryByName(String name);

    List<CategoryResponseDTO> getAllCategory();

    CategoryResponseDTO createCategory(CreateCategoryRequest category);

    CategoryResponseDTO updateCategory(Long id, UpdateCategoryRequest category);

    void deleteCategory(Long id);

}
