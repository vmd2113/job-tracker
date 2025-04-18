package com.duongw.commonservice.controller;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.item.CreateItemRequest;
import com.duongw.commonservice.model.dto.request.item.UpdateItemRequest;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.commonservice.service.IItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = ApiPath.API_ITEM)
public class ItemController {

    private final IItemService itemService;

    @Autowired
    public ItemController(IItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(path = "/")
    @Operation(summary = "get all data of items", description = "Send a request via this API to get all data of items")
    public ResponseEntity<ApiResponse<?>> getAllItem() {
        log.info("ITEM_CONTROLLER -> getAllItem");
        List<ItemResponseDTO> itemList = itemService.getAllItem();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-all.success"), itemList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "get data of item by id", description = "Send a request via this API to get data of item by id")
    public ResponseEntity<ApiResponse<?>> getItemById(@PathVariable(name = "id") Long id) {

        log.info("ITEM_CONTROLLER -> getItemById");
        ItemResponseDTO item = itemService.getItemById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-by-id.success"), item);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/name/{name}")
    @Operation(summary = "get data of item by name", description = "Send a request via this API to get data of item by name")
    public ResponseEntity<ApiResponse<?>> getItemByName(@PathVariable(name = "name") String name) {

        log.info("ITEM_CONTROLLER -> getItemByName");
        ItemResponseDTO item = itemService.getItemByName(name);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-by-name.success"), item);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping(path = "/code")
    @Operation(summary = "get data of item by name", description = "Send a request via this API to get data of item by name")
    public ResponseEntity<ApiResponse<?>> getItemByCode(@RequestParam(name = "code") String code) {

        log.info("ITEM_CONTROLLER -> getItemByCodee");
        ItemResponseDTO item = itemService.getItemByCode(code);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-by-name.success"), item);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/category/{categoryId}")
    @Operation(summary = "get data of item by category id", description = "Send a request via this API to get data of item by category id")
    public ResponseEntity<ApiResponse<?>> getItemByCategoryId(@PathVariable(name = "categoryId") Long categoryId) {

        log.info("ITEM_CONTROLLER -> getItemByCategoryId");
        List<ItemResponseDTO> itemList = itemService.getItemByCategoryId(categoryId);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-by-category-id.success"), itemList);
        return ResponseEntity.ok(apiResponse);
    }

    // get category status work
    @GetMapping(path = "/category/wo-status")
    @Operation(summary = "get data of item by category status-wo", description = "Send a request via this API to get data of item by category work-status")
    public ResponseEntity<ApiResponse<?>> getCategoryWorkStatus() {
        log.info("ITEM_CONTROLLER -> getCategoryStatus");
        List<ItemResponseDTO> itemList = itemService.getCategoryCode("WO_STATUS");
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-category-status.success"), itemList);
        return ResponseEntity.ok(apiResponse);
    }


    // get category status work
    @GetMapping(path = "/category/priority")
    @Operation(summary = "get data of item by category priority-wo", description = "Send a request via this API to get data of item by category priority-wo")
    public ResponseEntity<ApiResponse<?>> getCategoryStatus() {
        log.info("ITEM_CONTROLLER -> getCategoryWorkStatus");
        List<ItemResponseDTO> itemList = itemService.getCategoryCode("WO_PRIORITY");
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-category-status.success"), itemList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/parent/{parentItemId}")
    @Operation(summary = "get data of item by parent item id", description = "Send a request via this API to get data of item by parent item id")
    public ResponseEntity<ApiResponse<?>> getItemByParentItemId(@PathVariable(name = "parentItemId") Long parentItemId) {

        log.info("ITEM_CONTROLLER -> getItemByParentItemId");
        List<ItemResponseDTO> itemList = itemService.getItemByParentItemId(parentItemId);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.get-by-parent-item-id.success"), itemList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/search")
    @Operation(summary = "search item by specification", description = "Send a request via this API to search item by criteria")
    public ResponseEntity<ApiResponse<?>> searchItem(@RequestParam(name = "page", defaultValue = "1") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                                     @RequestParam(name = "itemName", required = false) String itemName,
                                                     @RequestParam(name = "itemCode", required = false) String itemCode,
                                                     @RequestParam(name = "categoryCode", required = false) String categoryCode,
                                                     @RequestParam(name = "sortBy", required = false, defaultValue = "updateDate") String sortBy,
                                                     @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {
        log.info("ITEM_CONTROLLER -> searchItemByCriteria");
        PageResponse<ItemResponseDTO> pageResponse = itemService.searchItems(itemName, itemCode, categoryCode, page, size, sortBy, sortDirection);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.search.success"), pageResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(path = "/")
    @Operation(summary = "create and add a item ", description = "Send a request via this API to create a new item")
    public ResponseEntity<ApiResponse<?>> createItem(@RequestBody CreateItemRequest item) {
        log.info("ITEM_CONTROLLER -> createItem");
        ItemResponseDTO item1 = itemService.createItem(item);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("item.create.success"), item1);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update item by id", description = "Send a request via this API to update item by id")
    public ResponseEntity<ApiResponse<?>> updateItem(@PathVariable(name = "id") Long id, @RequestBody UpdateItemRequest item) {
        log.info("ITEM_CONTROLLER -> updateItem");
        ItemResponseDTO item1 = itemService.updateItem(id, item);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.update.success"), item1);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping(path = "/{id}/status")
    @Operation(summary = "update item status by id", description = "Send a request via this API to update item status by id")
    public ResponseEntity<ApiResponse<?>> updateItemStatus(@PathVariable(name = "id") Long id, @RequestBody String status) {
        ItemResponseDTO item1 = itemService.updateItemStatus(id, status);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("item.update.success"), item1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteItem(@PathVariable(name = "id") Long id) {
        log.info("ITEM_CONTROLLER -> deleteItem");
        itemService.deleteItem(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("item.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/delete/list")
    public ResponseEntity<ApiResponse<?>> deleteListItem(@RequestBody List<Long> ids) {
        log.info("ITEM_CONTROLLER -> deleteListItem");
        itemService.deleteListItem(ids);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("item.delete.success"));
        return ResponseEntity.ok(apiResponse);
    }
}
