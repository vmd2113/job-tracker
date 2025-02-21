package com.duongw.commonservice.repository;


import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Item findByItemCode(String name);

    Item findByItemName(String name);

    List<Item> findByStatus(Long status);

    List<Item> findItemByCategory(Category category);

    List<Item> findByParentItemId(Long parentItemId);

    List<Item> findByItemCodeContaining(String itemCode);

    Item findByItemIdAndCategory_CategoryId(Long itemId, Long categoryId);


}
