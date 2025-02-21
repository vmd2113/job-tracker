package com.duongw.commonservice.service;

import com.duongw.commonservice.model.dto.request.configview.CreateConfigViewRequest;
import com.duongw.commonservice.model.dto.request.configview.UpdateConfigViewRequest;
import com.duongw.commonservice.model.dto.response.configview.ConfigViewResponseDTO;

import java.util.List;

public interface IConfigViewService {

    List<ConfigViewResponseDTO> getAllConfigView();

    List<ConfigViewResponseDTO> getConfigViewByRoleId(String roleId);

    List<ConfigViewResponseDTO> getConfigViewByStatus(String status);

    ConfigViewResponseDTO getConfigViewById(Long id);

    ConfigViewResponseDTO getConfigViewByName(String name);

    ConfigViewResponseDTO createConfigView(CreateConfigViewRequest configView);

    ConfigViewResponseDTO updateConfigView(Long id, UpdateConfigViewRequest configView);

    ConfigViewResponseDTO updateConfigViewStatus(Long id, String status);

    void deleteConfigView(Long id);
}
