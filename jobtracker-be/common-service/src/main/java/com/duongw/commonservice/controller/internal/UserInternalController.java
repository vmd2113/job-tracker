package com.duongw.commonservice.controller.internal;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.user.CreateUserRequest;
import com.duongw.commonservice.model.dto.request.user.RegisterRequest;
import com.duongw.commonservice.model.dto.response.user.UserDetailDTO;
import com.duongw.commonservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiPath.API_PATH_VERSION + "/internal/users")
public class UserInternalController {

    private final IUserService userService;

    @Autowired
    public UserInternalController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/by-id/{id}")
    public ResponseEntity<UserDetailDTO> getUserDetail(@PathVariable(name = "id") Long id) {
        UserDetailDTO userDetailDTO = userService.getUserDetail(id);
        return ResponseEntity.ok(userDetailDTO);
    }

    @GetMapping(path = "/by-username/{username}")
    public ResponseEntity<UserDetailDTO> getUserDetailByUsername(@PathVariable(name = "username") String username) {
        UserDetailDTO userDetailDTO = userService.getUserDetailByUsername(username);
        return ResponseEntity.ok(userDetailDTO);
    }


    @PostMapping(path = "/register")
    public ResponseEntity<UserDetailDTO> registerUser(@RequestBody RegisterRequest user) {
        UserDetailDTO userDetailDTO = userService.registerUser(user);
        return ResponseEntity.ok(userDetailDTO);

    }



}
