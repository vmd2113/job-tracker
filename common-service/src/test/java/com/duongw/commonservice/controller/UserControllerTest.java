package com.duongw.commonservice.controller;

import com.duongw.commonservice.model.dto.request.user.CreateUserRequest;
import com.duongw.commonservice.service.IUserService;
import com.duongw.commonservice.service.impl.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserService userService;
    private CreateUserRequest createUserRequest;
    private CreateUserRequest createUserRequestInit;



    @Test
    void createUser(@RequestBody CreateUserRequest createUserRequest) {
        // Given


        // When


        // Then
    }
}
