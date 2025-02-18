package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryCode(String code);
    Category findByCategoryName(String name);
    Optional<Category> findByCategoryId(Long id);
    List<Category> findByStatus(Long status);
    boolean existsByCategoryCode(String code);
    boolean existsByCategoryName(String name);

//    List<Category> searchCategoryCustomer

    @Query("SELECT c FROM Category c WHERE " +
            "(:categoryCode IS NULL OR LOWER(c.categoryCode) LIKE LOWER(CONCAT('%', :categoryCode, '%'))) AND " +
            "(:categoryName IS NULL OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%')))")
    Page<Category> searchCategories(
            @Param("categoryCode") String categoryCode,
            @Param("categoryName") String categoryName,
            Pageable pageable
    );


}
