package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.commonservice.model.dto.request.item.CreateItemRequest;
import com.duongw.commonservice.model.dto.request.item.UpdateItemRequest;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.commonservice.model.dto.response.role.RoleResponse;
import com.duongw.commonservice.model.entity.Item;
import com.duongw.commonservice.repository.ItemRepository;
import com.duongw.commonservice.service.IItemService;
import com.duongw.commonservice.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "ITEM_SERVICE")
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;
    private final IUserRoleService userRoleService;

    @Autowired
    public ItemService(ItemRepository itemRepository, IUserRoleService userRoleService) {
        this.itemRepository = itemRepository;
        this.userRoleService = userRoleService;
    }

    private ItemResponseDTO convertToItemResponseDTO(Item item) {
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setItemId(item.getItemId());
        itemResponseDTO.setItemName(item.getItemName());
        itemResponseDTO.setItemCode(item.getItemCode());
        itemResponseDTO.setItemValue(item.getItemValue());
        itemResponseDTO.setParentItemId(item.getParentItemId());
        itemResponseDTO.setCategoryId(item.getCategoryId());
        itemResponseDTO.setStatus(item.getStatus());
        return itemResponseDTO;
    }

    @Override
    public List<ItemResponseDTO> getAllItem() {
        List<Item> itemList = itemRepository.findAll();
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public List<ItemResponseDTO> getItemByCategoryId(Long categoryId) {
        List<Item> itemList = itemRepository.findByCategoryId(categoryId);
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public List<ItemResponseDTO> getItemByParentItemId(Long parentItemId) {
        List<Item> itemList = itemRepository.findByParentItemId(parentItemId);
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public ItemResponseDTO getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("item.not.found")));
        return convertToItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO getItemByName(String name) {
        Item item = itemRepository.findByItemName(name);
        return convertToItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO createItem(CreateItemRequest item) {
        Item newItem = new Item();
        newItem.setItemName(item.getItemName());
        newItem.setItemCode(item.getItemCode());
        newItem.setItemValue(item.getItemValue());
        newItem.setParentItemId(item.getParentItemId());
        newItem.setCategoryId(item.getCategoryId());
        newItem.setStatus(item.getStatus());
        return convertToItemResponseDTO(itemRepository.save(newItem));
    }

    @Override
    public ItemResponseDTO updateItem(Long id, UpdateItemRequest item) {
        Item updateItem = itemRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("item.not.found")));
        updateItem.setItemName(item.getItemName());
        updateItem.setItemCode(item.getItemCode());
        updateItem.setItemValue(item.getItemValue());
        updateItem.setParentItemId(item.getParentItemId());
        updateItem.setCategoryId(item.getCategoryId());
        updateItem.setStatus(item.getStatus());
        return convertToItemResponseDTO(itemRepository.save(updateItem));
    }

    @Override
    public ItemResponseDTO updateItemStatus(Long id, String status) {
        Item updateItem = itemRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("item.not.found")));
        //TODO: set status
        updateItem.setStatus(1L);
        return convertToItemResponseDTO(itemRepository.save(updateItem));
    }

    @Override
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("item.not.found")));
        itemRepository.delete(item);

    }

    @Override
    public RoleResponse convertToRoleResponseDTO(Long id) {
        log.info("ITEM_SERVICE  -> convertToRoleResponseDTO");
        log.info("Convert to RoleResponseDTO {}", id);
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("item.not.found")));

        log.info("GET: item.getItemCode(): {}", item.getItemCode());
        if (item.getItemCode().contains("ROLES_")) {
            return RoleResponse.builder()
                    .roleCode(item.getItemCode())
                    .roleName(item.getItemName())
                    .roleId(item.getItemId())
                    .build();
        } else {
            throw new RuntimeException(Translator.toLocate("item.not.role"));
        }

    }

    @Override
    public List<RoleResponse> getAllRoles() {
        log.info("ITEM_SERVICE  -> getAllRoles");
        List<Item> itemList = itemRepository.findByItemCodeContaining("ROLES_");
        if (itemList == null) {
            throw new RuntimeException(Translator.toLocate("item.not.found"));
        }
        return itemList.stream().map(role -> convertToRoleResponseDTO(role.getItemId())).toList();
    }

    @Override
    public List<RoleResponse> getRoleByUserId(Long userId) {
        log.info("ITEM_SERVICE  -> getRoleByUserId");
        List<Long> roleIdList = userRoleService.getRoleByUserId(userId);

        log.info("GET: roleIdList: {} ", roleIdList);
        if (roleIdList == null) {
            throw new RuntimeException(Translator.toLocate("item.not.found"));
        }
        return roleIdList.stream().map(this::convertToRoleResponseDTO).toList();

    }
}
