package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;
import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.repository.CategoryRepository;
import com.duongw.commonservice.repository.search.CategorySearchRepository;
import com.duongw.commonservice.service.ICategoryService;
import com.duongw.commonservice.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final CategorySearchRepository categorySearchRepository;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryValidator categoryValidator, CategorySearchRepository categorySearchRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
        this.categorySearchRepository = categorySearchRepository;
    }

    public CategoryResponseDTO convertToCategoryResponseDTO(Category category) {

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryId(category.getCategoryId());
        categoryResponseDTO.setCategoryCode(category.getCategoryCode());
        categoryResponseDTO.setCategoryName(category.getCategoryName());
        categoryResponseDTO.setStatus(category.getStatus());
        log.info("CATEGORY_SERVICE  -> convertToCategoryResponseDTO");
        log.info("CategoryResponseDTO: {}", categoryResponseDTO);
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
        log.info("CATEGORY_SERVICE  -> getCategoryById");
        return convertToCategoryResponseDTO(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.id"))));
    }

    @Override
    public CategoryResponseDTO getCategoryByName(String name) {
        log.info("CATEGORY_SERVICE  -> getCategoryByName");
        Category category = categoryRepository.findByCategoryName(name);
        if (category == null) {
            throw new ResourceNotFoundException(Translator.toLocate("category.not.found.name"));
        }
        return convertToCategoryResponseDTO(categoryRepository.findByCategoryName(name));
    }

    @Override
    public CategoryResponseDTO getCategoryByCode(String code) {
        return convertToCategoryResponseDTO(categoryRepository.findByCategoryCode(code).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.code"))));
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {
        log.info("CATEGORY_SERVICE  -> getAllCategory");
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(this::convertToCategoryResponseDTO).toList();
    }


    @Override

    public CategoryResponseDTO createCategory(CreateCategoryRequest category) {

        log.info("CATEGORY_SERVICE  -> createCategory");
        try {
            categoryValidator.validateCreateCategory(category);
            Category category1 = new Category();
            category1.setCategoryCode(category.getCategoryCode());
            category1.setCategoryName(category.getCategoryName());
            category1.setStatus(category.getStatus());
            //TODO: set user
            category1.setCreatedByUser(1L);
            log.info("CATEGORY_SERVICE  -> createCategory success");
            return convertToCategoryResponseDTO(categoryRepository.save(category1));
        } catch (AlreadyExistedException e) {
            throw new AlreadyExistedException(e.getMessage());
        }
    }


    @Override
    public CategoryResponseDTO updateCategory(Long id, UpdateCategoryRequest category) {
        try {
            log.info("CATEGORY_SERVICE  -> updateCategory");
            Category updateCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found")));
            categoryValidator.validateUpdateCategory(category, id);
            updateCategory.setCategoryCode(category.getCategoryCode());
            updateCategory.setCategoryName(category.getCategoryName());
            updateCategory.setStatus(category.getStatus());
            //TODO: set user
            updateCategory.setUpdatedByUser(1L);
            log.info("CATEGORY_SERVICE  -> updateCategory success");
            return convertToCategoryResponseDTO(categoryRepository.save(updateCategory));
        } catch (AlreadyExistedException e) {
            throw new AlreadyExistedException(e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("CATEGORY_SERVICE  -> deleteCategory with id: {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.id")));
        categoryRepository.delete(category);

    }

    @Override
    public void deleteCategoryList(List<Long> ids) {
        log.info("CATEGORY_SERVICE  -> deleteCategoryList");
        for (Long id : ids) {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.id")));
            categoryRepository.delete(category);
        }

    }

    @Override
    public PageResponse<CategoryResponseDTO> searchCategories(String code, String name, int pageNo, int pageSize, String sortBy, String sortDirect) {
        log.info("CATEGORY_SERVICE  -> searchCategories");
        PageResponse<CategoryResponseDTO> pageResponse = categorySearchRepository.searchCategory(code, name, pageNo, pageSize, sortBy, sortDirect);
        return pageResponse;

    }
}
