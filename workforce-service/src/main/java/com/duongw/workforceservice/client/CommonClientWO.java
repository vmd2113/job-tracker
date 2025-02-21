package com.duongw.workforceservice.client;


import com.duongw.workforceservice.model.dto.response.item.ItemResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "common-service", url = "http://localhost:8001" + "/api/v1/internal")
public interface CommonClientWO {

    // ITEM

//    @GetMapping(path = "/items/roles")
//    List<ItemResponseDTO> getAllRoles();
//
//    @GetMapping(path = "/items/roles/by-id/{userId}")
//    List<RoleResponseDTO> getRoleByUserId(@PathVariable(name = "userId") Long userId);

    @GetMapping("/items/by-id/{itemId}")
    ItemResponseDTO getItemById(@PathVariable(name = "itemId") Long itemId);

    @GetMapping("/items/category/by-id/{itemId}/categoryId/{categoryId}")
    ItemResponseDTO getItemByIdAndCategoryId(@PathVariable(name = "itemId") Long itemId, @PathVariable(name = "categoryId") Long categoryId);


}
