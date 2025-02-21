package com.duongw.commonservice.validator;

import com.duongw.commonservice.model.entity.Item;
import com.duongw.commonservice.repository.ItemRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemValidator {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemValidator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void validateDuplicateInfo(String itemCode, long itemId){
        Item existingItemByCode = itemRepository.findByItemCode(itemCode);
        if (existingItemByCode != null && existingItemByCode.getItemId() != itemId) {
            throw new IllegalArgumentException("Duplicate item code");
        }
    }
}
