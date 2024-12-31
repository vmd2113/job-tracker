package com.duongw.commonservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.response.category.CategoryResponseDTO;
import com.duongw.commonservice.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_CATEGORY)
public class CategoryController {

    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/")
    @Operation(summary = "get all data of category", description = "Send a request via this API to get all data of category")

    public ResponseEntity<ApiResponse<?>> getAllCategory() {
        List<CategoryResponseDTO> categoryList = categoryService.getAllCategory();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.get-all.success"), categoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "get category by id", description = "Send a request via this API to get data of category by id")

    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable(name = "id") Long id) {

        CategoryResponseDTO category = categoryService.getCategoryById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "get category by id success", category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/name")
    @Operation(summary = "get data of category by name", description = "Send a request via this API to get  data of category by name")

    public ResponseEntity<ApiResponse<?>> getCategoryByName(@RequestParam(name = "name") String name) {

        CategoryResponseDTO category = categoryService.getCategoryByName(name);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "get category by name success", category);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PostMapping(path = "/")
    @Operation(summary = "create and add a category ", description = "Send a request via this API to create a new category")

    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody CreateCategoryRequest category) {

        CategoryResponseDTO category1 = categoryService.createCategory(category);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, "create category success", category1);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update category by id", description = "Send a request via this API to update category by id")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable(name = "id") Long id, @RequestBody UpdateCategoryRequest category) {

        CategoryResponseDTO category1 = categoryService.updateCategory(id, category);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("category.update.success"), category1);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete category by id", description = "Send a request via this API to delete category by id")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable(name = "id") Long id) {

        categoryService.deleteCategory(id);

        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "delete category success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

}
