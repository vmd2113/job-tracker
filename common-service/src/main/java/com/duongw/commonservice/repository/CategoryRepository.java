package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByCategoryCode(String code);
    Category findByCategoryName(String name);
    Optional<Category> findByCategoryId(Long id);
    List<Category> findByStatus(Long status);
    boolean existsByCategoryCode(String code);
    boolean existsByCategoryName(String name);



}
