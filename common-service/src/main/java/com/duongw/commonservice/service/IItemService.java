package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.item.CreateItemRequest;
import com.duongw.commonservice.model.dto.request.item.UpdateItemRequest;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;

import java.util.List;

public interface IItemService {
    List<ItemResponseDTO> getAllItem();
    List<ItemResponseDTO> getItemByCategoryId(Long categoryId);
    List<ItemResponseDTO> getItemByParentItemId(Long parentItemId);

    ItemResponseDTO getItemById(Long id);
    ItemResponseDTO getItemByName(String name);
    ItemResponseDTO createItem(CreateItemRequest item);
    ItemResponseDTO updateItem(Long id, UpdateItemRequest item);
    ItemResponseDTO updateItemStatus(Long id, String status);
    void deleteItem(Long id);


}
