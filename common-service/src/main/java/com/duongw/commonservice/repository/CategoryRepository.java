package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryCode(String code);
    Category findByCategoryName(String name);
    List<Category> findByStatus(Long status);

    boolean existsByCategoryCode(String code);
    boolean existsByCategoryName(String name);

}
