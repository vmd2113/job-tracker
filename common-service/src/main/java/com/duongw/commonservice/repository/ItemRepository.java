package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemCode(String name);
    Item findByItemName(String name);
    List<Item> findByStatus(Long status);
    List<Item> findByCategoryId(Long categoryId);
    List<Item> findByParentItemId(Long parentItemId);
    List<Item> findByItemCodeContaining(String itemCode);

    boolean existsByItemCode(String itemCode);
    boolean existsByItemName(String itemName);





}
