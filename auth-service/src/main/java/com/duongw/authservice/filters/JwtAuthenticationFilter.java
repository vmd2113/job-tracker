package com.duongw.authservice.filters;

import com.duongw.authservice.model.dto.response.RoleResponseDTO;
import com.duongw.authservice.model.entity.AuthUserDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "common-service", url = "http://localhost:8001" + "/api/v1/internal")
public class JwtAuthenticationFilter {


}
