package com.duongw.commonservice.controller.internal;

import com.duongw.common.constant.ApiPath;
import com.duongw.commonservice.model.dto.response.role.RoleResponse;
import com.duongw.commonservice.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = ApiPath.API_PATH_VERSION + "/internal/items")
public class ItemInternalController {
    private final IItemService itemService;

    @Autowired
    public ItemInternalController(IItemService itemService) {
        this.itemService = itemService;

    }

    @GetMapping(path = "/roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roleList = itemService.getAllRoles();
        return ResponseEntity.ok(roleList);
    }

    @GetMapping(path = "/roles/by-id/{userId}")
    public ResponseEntity<List<RoleResponse>> getRoleByUserId(@PathVariable(name = "userId") Long userId) {
        List<RoleResponse> roleList = itemService.getRoleByUserId(userId);
        return ResponseEntity.ok(roleList);
    }
}
