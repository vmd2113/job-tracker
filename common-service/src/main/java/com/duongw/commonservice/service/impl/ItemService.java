package com.duongw.commonservice.service.impl;


import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.item.CreateItemRequest;
import com.duongw.commonservice.model.dto.request.item.UpdateItemRequest;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.commonservice.model.dto.response.role.RoleResponse;
import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.model.entity.Item;
import com.duongw.commonservice.repository.CategoryRepository;
import com.duongw.commonservice.repository.ItemRepository;
import com.duongw.commonservice.repository.search.ItemSearchRepository;
import com.duongw.commonservice.service.IItemService;
import com.duongw.commonservice.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;
    private final IUserRoleService userRoleService;
    private final CategoryRepository categoryRepository;
    private final ItemSearchRepository itemSearchRepository;


    @Autowired
    public ItemService(ItemRepository itemRepository, IUserRoleService userRoleService, CategoryRepository categoryRepository, ItemSearchRepository itemSearchRepository) {
        this.itemRepository = itemRepository;
        this.userRoleService = userRoleService;
        this.categoryRepository = categoryRepository;
        this.itemSearchRepository = itemSearchRepository;
    }

    private ItemResponseDTO convertToItemResponseDTO(Item item) {
        log.info("ITEM_SERVICE  -> convertToItemResponseDTO");
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setItemId(item.getItemId());
        itemResponseDTO.setItemName(item.getItemName());
        itemResponseDTO.setItemCode(item.getItemCode());
        itemResponseDTO.setItemValue(item.getItemValue());
        itemResponseDTO.setParentItemId(item.getParentItemId());


        itemResponseDTO.setCategoryId(item.getCategory().getCategoryId());
        itemResponseDTO.setCategoryCode(item.getCategory().getCategoryCode());
        itemResponseDTO.setCategoryName(item.getCategory().getCategoryName());
        itemResponseDTO.setStatus(item.getStatus());
        return itemResponseDTO;
    }

    @Override
    public List<ItemResponseDTO> getAllItem() {
        log.info("ITEM_SERVICE  -> getAllItem");
        List<Item> itemList = itemRepository.findAll();
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public List<ItemResponseDTO> getItemByCategoryId(Long categoryId) {
        log.info("ITEM_SERVICE  -> getItemByCategoryId");
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.id")));
        List<Item> itemList = itemRepository.findItemByCategory(category);
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public List<ItemResponseDTO> getCategoryCode(String categoryCode) {
        log.info("ITEM_SERVICE  -> getCategoryCode");
        Category category = categoryRepository.findByCategoryCode(categoryCode).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.code")));
        List<Item> itemList = itemRepository.findItemByCategory(category);
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public List<ItemResponseDTO> getItemByParentItemId(Long parentItemId) {
        log.info("ITEM_SERVICE  -> getItemByParentItemId");
        List<Item> itemList = itemRepository.findByParentItemId(parentItemId);
        return itemList.stream().map(this::convertToItemResponseDTO).toList();
    }

    @Override
    public ItemResponseDTO getItemById(Long id) {
        log.info("ITEM_SERVICE  -> getItemById");
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("item.not.found.id")));
        return convertToItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO getItemByName(String name) {
        log.info("ITEM_SERVICE  -> getItemByName");
        Item item = itemRepository.findByItemName(name);
        return convertToItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO getItemByCode(String code) {
        log.info("ITEM_SERVICE  -> getItemByCode");
        Item item = itemRepository.findByItemCode(code);
        return convertToItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO createItem(CreateItemRequest item) {
        log.info("ITEM_SERVICE  -> createItem");
        Item newItem = new Item();
        newItem.setItemName(item.getItemName());
        newItem.setItemCode(item.getItemCode());
        newItem.setItemValue(item.getItemValue());
        if (item.getParentItemId() != null) {
            newItem.setParentItemId(item.getParentItemId());
        }
        newItem.setParentItemId(item.getParentItemId());


        Category category = categoryRepository.findByCategoryId(item.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.id")));
        newItem.setCategory(category);


        newItem.setStatus(item.getStatus());
        return convertToItemResponseDTO(itemRepository.save(newItem));
    }

    @Override
    public ItemResponseDTO updateItem(Long id, UpdateItemRequest item) {
        log.info("ITEM_SERVICE  -> updateItem");
        Item updateItem = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("item.not.found.id")));
        updateItem.setItemName(item.getItemName());
        updateItem.setItemCode(item.getItemCode());
        updateItem.setItemValue(item.getItemValue());
        updateItem.setParentItemId(item.getParentItemId());

        Category category = categoryRepository.findByCategoryId(item.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("category.not.found.id")));
        updateItem.setCategory(category);
        updateItem.setStatus(item.getStatus());
        return convertToItemResponseDTO(itemRepository.save(updateItem));
    }

    @Override
    public ItemResponseDTO updateItemStatus(Long id, String status) {
        log.info("ITEM_SERVICE  -> updateItemStatus");
        Item updateItem = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("item.not.found.id")));
        //TODO: set status
        updateItem.setStatus(1L);
        return convertToItemResponseDTO(itemRepository.save(updateItem));
    }

    @Override
    public void deleteItem(Long id) {
        log.info("ITEM_SERVICE  -> deleteItem");
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("item.not.found.id")));
        itemRepository.delete(item);


    }

    @Override
    public void deleteListItem(List<Long> ids) {
        log.info("ITEM_SERVICE  -> deleteListItem");
        for (Long id : ids) {
            Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("item.not.found.id")));
            itemRepository.delete(item);
        }
    }

    @Override
    public RoleResponse convertToRoleResponseDTO(Long id) {
        log.info("ITEM_SERVICE  -> convertToRoleResponseDTO");
        log.info("Convert to RoleResponseDTO {}", id);
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("item.not.found.id")));

        log.info("GET: item.getItemCode(): {}", item.getItemCode());
        if (item.getItemCode().contains("ROLES_")) {
            return RoleResponse.builder()
                    .roleCode(item.getItemCode())
                    .roleName(item.getItemName())
                    .roleId(item.getItemId())
                    .build();
        } else {
            throw new ResourceNotFoundException(Translator.toLocate("item.not.role"));
        }

    }

    @Override
    public List<RoleResponse> getAllRoles() {
        log.info("ITEM_SERVICE  -> getAllRoles");
        List<Item> itemList = itemRepository.findByItemCodeContaining("ROLES_");
        if (itemList == null) {
            throw new ResourceNotFoundException(Translator.toLocate("item.not.found.code"));
        }
        return itemList.stream().map(role -> convertToRoleResponseDTO(role.getItemId())).toList();
    }

    @Override
    public List<RoleResponse> getRoleByUserId(Long userId) {
        log.info("ITEM_SERVICE  -> getRoleByUserId");
        List<Long> roleIdList = userRoleService.getRoleByUserId(userId);

        log.info("GET: roleIdList: {} ", roleIdList);
        if (roleIdList == null) {
            throw new ResourceNotFoundException(Translator.toLocate("item.not.found.id"));
        }
        return roleIdList.stream().map(this::convertToRoleResponseDTO).toList();

    }

    @Override
    public ItemResponseDTO getItemByIdAndCategoryId(Long itemId, Long categoryId) {
        try {
            log.info("ITEM_SERVICE  -> getItemByIdAndCategoryId");
            Item item = itemRepository.findByItemIdAndCategory_CategoryId(itemId, categoryId);
            return convertToItemResponseDTO(item);
        }catch (Exception e){
            log.error("ITEM_SERVICE  -> getItemByIdAndCategoryId fail");
            throw new ResourceNotFoundException(Translator.toLocate("item.not.found.id"));
        }

    }

    @Override
    public PageResponse<ItemResponseDTO> searchItems(String itemName, String itemCode, String categoryCode, int pageNo, int pageSize, String sortBy, String sortDirection) {
        return itemSearchRepository.searchItems(itemName, itemCode, categoryCode, pageNo, pageSize, sortBy, sortDirection);
    }



}
