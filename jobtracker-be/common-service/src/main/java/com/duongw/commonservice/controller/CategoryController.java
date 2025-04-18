package com.duongw.commonservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;
import com.duongw.commonservice.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_CATEGORY)
@Slf4j
public class CategoryController {

    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;

    }

    @GetMapping(path = "/")
    @Operation(summary = "get all data of category", description = "Send a request via this API to get all data of category")

    public ResponseEntity<ApiResponse<?>> getAllCategory() {
        log.info("CATEGORY_CONTROLLER  -> getAllCategory");
        List<CategoryResponseDTO> categoryList = categoryService.getAllCategory();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.get-all.success"), categoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "get category by id", description = "Send a request via this API to get data of category by id")

    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable(name = "id") Long id) {
        log.info("CATEGORY_CONTROLLER  -> getCategoryById");
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.get-by-id.success"), category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/name")
    @Operation(summary = "get data of category by name", description = "Send a request via this API to get  data of category by name")
    public ResponseEntity<ApiResponse<?>> getCategoryByName(@RequestParam(name = "name") String name) {
        log.info("CATEGORY_CONTROLLER  -> getCategoryByName");
        CategoryResponseDTO category = categoryService.getCategoryByName(name);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.get-by-name.success"), category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/code")
    @Operation(summary = "get data of category by code", description = "Send a request via this API to get  data of category by code")
    public ResponseEntity<ApiResponse<?>> getCategoryByCode(@RequestParam(name = "code") String code) {
        log.info("CATEGORY_CONTROLLER  -> getCategoryByCode");
        CategoryResponseDTO category = categoryService.getCategoryByCode(code);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.get-by-code.success"), category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/")
    @Operation(summary = "create and add a category ", description = "Send a request via this API to create a new category")
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody CreateCategoryRequest category) {
        log.info("CATEGORY_CONTROLLER  -> createCategory");
        CategoryResponseDTO category1 = categoryService.createCategory(category);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("category.create.success"), category1);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update category by id", description = "Send a request via this API to update category by id")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable(name = "id") Long id, @RequestBody UpdateCategoryRequest category) {
        log.info("CATEGORY_CONTROLLER  -> updateCategory");
        CategoryResponseDTO category1 = categoryService.updateCategory(id, category);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.update.success"), category1);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete category by id", description = "Send a request via this API to delete category by id")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable(name = "id") Long id) {
        log.info("CATEGORY_CONTROLLER  -> deleteCategory");
        categoryService.deleteCategory(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("category.delete.success"));

        log.info("CATEGORY_CONTROLLER  -> deleteCategory success");
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);

    }

    @DeleteMapping(path = "/delete/list")
    @Operation(summary = "delete category list", description = "Send a request via this API to delete category list")
    public ResponseEntity<ApiResponse<?>> deleteCategoryList(@RequestBody List<Long> ids) {
        log.info("CATEGORY_CONTROLLER  -> deleteCategoryList");
        categoryService.deleteCategoryList(ids);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("category.delete.success"));
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchCategories(@RequestParam(required = false, name = "categoryCode") String categoryCode,
                                                           @RequestParam(required = false, name = "categoryName") String categoryName,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sortBy", defaultValue = "updateDate") String sortBy,
                                                           @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDir) {

        // Convert "sort" parameter into Pageable
        log.info("CATEGORY_CONTROLLER  -> searchCategories");

        PageResponse<CategoryResponseDTO> pageResponse = categoryService.searchCategories(categoryCode, categoryName, page, size, sortBy, sortDir);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.search.success"), pageResponse);
        log.info("CATEGORY_CONTROLLER  -> searchCategories success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }


}
