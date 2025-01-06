package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.commonservice.model.dto.request.item.CreateItemRequest;
import com.duongw.commonservice.model.dto.request.item.UpdateItemRequest;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.commonservice.model.dto.response.role.RoleResponse;
import com.duongw.commonservice.model.entity.Item;
import com.duongw.commonservice.repository.ItemRepository;
import com.duongw.commonservice.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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
        return null;
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return List.of();
    }

    @Override
    public List<RoleResponse> getRoleByUserId(Long userId) {
        return List.of();
    }
}
