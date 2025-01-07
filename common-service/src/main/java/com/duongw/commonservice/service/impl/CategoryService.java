package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.commonservice.model.dto.request.category.SearchCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;
import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.repository.CategoryRepository;
import com.duongw.commonservice.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO convertToCategoryResponseDTO(Category category) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryId(category.getCategoryId());
        categoryResponseDTO.setCategoryCode(category.getCategoryCode());
        categoryResponseDTO.setCategoryName(category.getCategoryName());
        categoryResponseDTO.setStatus(category.getStatus());
        return categoryResponseDTO;
    }

    public Category convertToCategory(CategoryResponseDTO categoryResponseDTO) {
        Category category = new Category();
        category.setCategoryId(categoryResponseDTO.getCategoryId());
        category.setCategoryCode(categoryResponseDTO.getCategoryCode());
        category.setCategoryName(categoryResponseDTO.getCategoryName());
        category.setStatus(categoryResponseDTO.getStatus());
        return category;
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        return convertToCategoryResponseDTO(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found"))));
    }

    @Override
    public CategoryResponseDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByCategoryName(name);
        if (category == null) {
            throw new ResourceNotFoundException(Translator.toLocate("category.not.found"));
        }
        return convertToCategoryResponseDTO(categoryRepository.findByCategoryName(name));
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(this::convertToCategoryResponseDTO).toList();
    }

    private boolean validateCategory(String categoryCode, String categoryName) {
        if (categoryRepository.existsByCategoryCode(categoryCode)) {
            throw new AlreadyExistedException(Translator.toLocate("category.exist.code"));
        }
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new AlreadyExistedException(Translator.toLocate("category.exist.name"));
        }
        return true;
    }

    @Override
    public CategoryResponseDTO createCategory(CreateCategoryRequest category) {
        if (!validateCategory(category.getCategoryCode(), category.getCategoryName())) {
            throw new AlreadyExistedException(Translator.toLocate("category.exist"));
        }
        Category category1 = new Category();
        category1.setCategoryCode(category.getCategoryCode());
        category1.setCategoryName(category.getCategoryName());
        category1.setStatus(category.getStatus());
        //TODO: set user
        category1.setCreatedByUser(1L);
        return convertToCategoryResponseDTO(categoryRepository.save(category1));
    }


    @Override
    public CategoryResponseDTO updateCategory(Long id, UpdateCategoryRequest category) {

        Category updateCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found")));
        if (!validateCategory(category.getCategoryCode(), category.getCategoryName())) {
            throw new AlreadyExistedException(Translator.toLocate("category.exist"));
        }
        updateCategory.setCategoryCode(category.getCategoryCode());
        updateCategory.setCategoryName(category.getCategoryName());
        updateCategory.setStatus(category.getStatus());
        //TODO: set user
        updateCategory.setUpdatedByUser(1L);
        return convertToCategoryResponseDTO(categoryRepository.save(updateCategory));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found")));
        categoryRepository.delete(category);

    }



    @Override
    public PageResponse<CategoryResponseDTO> searchCategories(String code, String name, Pageable pageable) {
        // Truy vấn từ repository trả về Page<Category>
        Page<Category> page = categoryRepository.searchCategories(code, name, pageable);
        List<CategoryResponseDTO> categoryResponseDTOList = page.getContent().stream().map(this::convertToCategoryResponseDTO).toList();
        return PageResponseConverter.convertFromList(categoryResponseDTOList, page.getTotalElements());

    }
}
