package com.duongw.commonservice.controller;

import com.duongw.common.constant.ApiPath;
import com.duongw.common.model.dto.response.ApiResponse;
import com.duongw.common.config.i18n.Translator;
import com.duongw.commonservice.model.dto.request.user.LoginRequest;
import com.duongw.commonservice.model.dto.request.user.RegisterUserRequest;
import com.duongw.commonservice.model.dto.request.user.UpdateUserRequest;
import com.duongw.commonservice.model.dto.response.user.UserLoginResponse;
import com.duongw.commonservice.model.dto.response.user.UserResponseDTO;
import com.duongw.commonservice.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = ApiPath.API_USER)
public class UserController {


    private final IUserService userService;


    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/")
    @Operation(summary = "get all data of user", description = "Send a request via this API to get all data of user")
    public ResponseEntity<ApiResponse<?>> getAllUser() {
        List<UserResponseDTO> userList = userService.getAllUser();
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.get-all.success"), userList);
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "find user by id", description = "Send a request via this API to find user by id")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable(name = "id") Long id) {
        UserResponseDTO user = userService.getUserById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.get-by-id.success"), user);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/name")
    @Operation(summary = "find user by username", description = "Send a request via this API to find user by username")

    public ResponseEntity<ApiResponse<?>> getUserByUsername(@RequestParam(name = "username") String username) {
        UserResponseDTO user = userService.getUserByUsername(username);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.get-by-username.success"), user);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/email")
    @Operation(summary = "find user by email", description = "Send a request via this API to find user by email")
    public ResponseEntity<ApiResponse<?>> getUserByEmail(@RequestParam(name = "email") String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.get-by-email.success"), user);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(path = "/phone")
    @Operation(summary = "find user by phone number", description = "Send a request via this API to find user by phone number")

    public ResponseEntity<ApiResponse<?>> getUserByPhoneNumber(@RequestParam(name = "phoneNumber") String phoneNumber) {
        UserResponseDTO user = userService.getUserByPhoneNumber(phoneNumber);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.get-by-phone-number.success"), user);
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping(path = "/")
    @Operation(summary = "create user", description = "Send a request via this API to create a new user")
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody RegisterUserRequest user) {
        UserResponseDTO user1 = userService.createUser(user);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.CREATED, Translator.toLocate("user.create.success"), user1);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update user", description = "Send a request via this API to update user")

    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable(name = "id") Long id, @Valid @RequestBody UpdateUserRequest user) {
        UserResponseDTO user1 = userService.updateUser(id, user);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.update.success"), user1);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete user", description = "Send a request via this API to delete user")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NO_CONTENT, Translator.toLocate("user.delete.success"));
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }


    @GetMapping(path = "/validate")
    @Operation(summary = "validate user", description = "Send a request via this API to validate user")
    public ResponseEntity<ApiResponse<UserLoginResponse>> validateUser(@RequestBody LoginRequest loginRequest) {
        UserLoginResponse userLoginResponse = userService.validateUser(loginRequest);
        ApiResponse<UserLoginResponse> apiResponse = new ApiResponse<>(HttpStatus.OK, Translator.toLocate("user.validate.success"), userLoginResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
