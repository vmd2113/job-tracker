package com.duongw.commonservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.item.CreateItemRequest;
import com.duongw.commonservice.model.dto.request.item.UpdateItemRequest;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.commonservice.model.dto.response.role.RoleResponse;

import java.util.List;

public interface IItemService {
    List<ItemResponseDTO> getAllItem();

    List<ItemResponseDTO> getItemByCategoryId(Long categoryId);

    List<ItemResponseDTO> getItemByParentItemId(Long parentItemId);

    ItemResponseDTO getItemById(Long id);

    ItemResponseDTO getItemByName(String name);

    ItemResponseDTO getItemByCode(String code);

    ItemResponseDTO createItem(CreateItemRequest item);

    ItemResponseDTO updateItem(Long id, UpdateItemRequest item);

    ItemResponseDTO updateItemStatus(Long id, String status);

    void deleteItem(Long id);

    void deleteListItem(List<Long> ids);

    RoleResponse convertToRoleResponseDTO(Long id);

    List<RoleResponse> getAllRoles();

    List<RoleResponse> getRoleByUserId(Long userId);

    PageResponse<ItemResponseDTO> searchItems(String itemName, String itemCode, String categoryCode, int pageNo, int pageSize, String sortBy, String sortDirection);
}



