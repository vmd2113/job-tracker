package com.duongw.commonservice.validator;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.commonservice.model.dto.request.category.CreateCategoryRequest;
import com.duongw.commonservice.model.dto.request.category.UpdateCategoryRequest;
import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CategoryValidator {

    private final CategoryRepository categoryRepository;

    public CategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Kiểm tra trùng lặp thông tin categoryCode và categoryName.
     *
     * @param categoryCode Mã danh mục cần kiểm tra.
     * @param categoryName Tên danh mục cần kiểm tra.
     * @param excludeId    ID của danh mục hiện tại (null nếu là tạo mới).
     * @throws AlreadyExistedException nếu thông tin trùng lặp.
     */
    public void validateDuplicateInfo(String categoryCode, String categoryName, Long excludeId) {
        log.info("Validating duplicate info for category. ExcludeId: {}", excludeId);

        // Kiểm tra duplicate categoryCode với Optional
        Optional<Category> existingCategoryByCodeOpt = categoryRepository.findByCategoryCode(categoryCode);
        if (existingCategoryByCodeOpt.isPresent() &&
                (excludeId == null || !existingCategoryByCodeOpt.get().getCategoryId().equals(excludeId))) {
            throw new AlreadyExistedException(Translator.toLocate("validate.categoryCode.exist"));
        }

        // Kiểm tra duplicate categoryName
        Category existingCategoryByName = categoryRepository.findByCategoryName(categoryName);
        if (existingCategoryByName != null &&
                (excludeId == null || !existingCategoryByName.getCategoryId().equals(excludeId))) {
            throw new AlreadyExistedException(Translator.toLocate("validate.categoryName.exist"));
        }

        log.info("Duplicate info validation passed");
    }

    /**
     * Validate thông tin khi tạo mới danh mục với dữ liệu truyền vào trực tiếp.
     *
     * @param categoryCode Mã danh mục.
     * @param categoryName Tên danh mục.
     * @param id           ID danh mục (thường là null khi tạo mới).
     */
    public void validateCreateCategory(String categoryCode, String categoryName, Long id) {
        log.info("Validating create category with code: {} and name: {}", categoryCode, categoryName);
        validateDuplicateInfo(categoryCode, categoryName, id);
    }

    /**
     * Validate thông tin khi tạo mới danh mục từ đối tượng request.
     *
     * @param request Yêu cầu tạo mới danh mục.
     */
    public void validateCreateCategory(CreateCategoryRequest request) {
        log.info("Validating create category request");
        validateDuplicateInfo(request.getCategoryCode(), request.getCategoryName(), null);
    }

    /**
     * Validate thông tin khi cập nhật danh mục.
     *
     * @param request Yêu cầu cập nhật danh mục.
     * @param id      ID của danh mục đang cập nhật.
     */
    public void validateUpdateCategory(UpdateCategoryRequest request, Long id) {
        log.info("Validating update category request for category id: {}", id);
        validateDuplicateInfo(request.getCategoryCode(), request.getCategoryName(), id);
    }
}
