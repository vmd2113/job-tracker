package com.duongw.authservice.client;

import com.duongw.authservice.model.dto.response.RoleResponseDTO;
import com.duongw.authservice.model.entity.AuthUserDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "common-service", url = "http://localhost:8001" + "/api/v1/internal")
public interface CommonClient {

    @GetMapping(path = "/users/by-id/{id}")
    AuthUserDetail getUserDetail(@PathVariable(name = "id") Long id);

    @GetMapping(path =  "/users/by-username/{username}")
    Optional<AuthUserDetail> getUserDetailByUsername(@PathVariable(name = "username") String username);

    // ITEM

    @GetMapping(path =  "/items/roles")
    List<RoleResponseDTO> getAllRoles();

    @GetMapping(path =  "/items/roles/by-id/{userId}")
    List<RoleResponseDTO> getRoleByUserId(@PathVariable(name = "userId") Long userId);

}
