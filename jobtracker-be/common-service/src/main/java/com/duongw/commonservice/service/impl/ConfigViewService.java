package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.commonservice.model.dto.request.configview.CreateConfigViewRequest;
import com.duongw.commonservice.model.dto.request.configview.UpdateConfigViewRequest;
import com.duongw.commonservice.model.dto.response.configview.ConfigViewResponseDTO;
import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.repository.ConfigViewRepository;
import com.duongw.commonservice.service.IConfigViewService;
import com.duongw.commonservice.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConfigViewService implements IConfigViewService {

    private final ConfigViewRepository configViewRepository;

    private final IItemService itemService;

    @Autowired
    public ConfigViewService(ConfigViewRepository configViewRepository, IItemService itemService) {
        this.configViewRepository = configViewRepository;
        this.itemService = itemService;
    }

    private ConfigViewResponseDTO convertToConfigViewResponseDTO(ConfigView configView) {
        ConfigViewResponseDTO configViewResponseDTO = new ConfigViewResponseDTO();
        configViewResponseDTO.setId(configView.getId());
        configViewResponseDTO.setViewName(configView.getViewName());
        configViewResponseDTO.setViewPath(configView.getViewPath());
        configViewResponseDTO.setApiPath(configView.getApiPath());
        configViewResponseDTO.setRoleId(configView.getRoleId());
        configViewResponseDTO.setStatus(configView.getStatus());
        return configViewResponseDTO;
    }

    private ConfigView convertToConfigView(ConfigViewResponseDTO configViewResponseDTO) {
        ConfigView configView = new ConfigView();
        configView.setViewName(configViewResponseDTO.getViewName());
        configView.setViewPath(configViewResponseDTO.getViewPath());
        configView.setApiPath(configViewResponseDTO.getApiPath());
        configView.setRoleId(configViewResponseDTO.getRoleId());
        configView.setStatus(configViewResponseDTO.getStatus());
        return configView;
    }

    @Override
    public List<ConfigViewResponseDTO> getAllConfigView() {
        List<ConfigView> configViewList = configViewRepository.findAll();
        return configViewList.stream().map(this::convertToConfigViewResponseDTO).toList();
    }


    private boolean hasRole(List<String> roleCodes, ConfigView configView) {


        log.info("CONFIG_VIEW_SERVICE -> hasRole with input roleIds: "  );

        // Kiểm tra dữ liệu roleId trong configView
        String roleConfigView = configView.getRoleId();
        if (roleConfigView == null || roleConfigView.isEmpty()) {
            return false;
        }

        List<String> roleId = roleCodes.stream().map(code-> itemService.getItemByCode(code).getItemId().toString()).toList();

        // Tách các roleId của người dùng thành mảng


        // Tách các roleId được cấu hình trong configView thành mảng
        String[] configRoleIds = roleConfigView.split(",");

        // Kiểm tra xem có bất kỳ roleId nào của người dùng khớp với roleId trong configView không
        for (String userRoleId : roleId) {
            String trimmedUserRoleId = userRoleId.trim();

            for (String configRoleId : configRoleIds) {
                String trimmedConfigRoleId = configRoleId.trim();

                log.info("Comparing user role: " + trimmedUserRoleId + " with config role: " + trimmedConfigRoleId);

                if (trimmedUserRoleId.equals(trimmedConfigRoleId)) {
                    log.info("Found matching role: " + trimmedUserRoleId);
                    log.info("SUCCESS");
                    return true; // Nếu có ít nhất một roleId khớp, trả về true
                }
            }
        }

        return false; // Không có roleId nào khớp
    }

    @Override
    public List<ConfigViewResponseDTO> getConfigViewByRoleCode(List<String >roleCode) {
        log.info("CONFIG_VIEW_SERVICE  -> getConfigViewByRoleId");
        List<ConfigView> configViewList = configViewRepository.findAll();

        log.info("CONFIG_VIEW_SERVICE  -> getConfigViewByRoleId" + configViewList.size());
        List<ConfigView> filteredByRoleId = configViewList
                .stream()
                .filter(conf -> hasRole(roleCode, conf))
                .toList();

        if (filteredByRoleId.isEmpty()) {
            return null;
        }

        return filteredByRoleId.stream()
                .map(this::convertToConfigViewResponseDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<ConfigViewResponseDTO> getConfigViewByStatus(String status) {
        //TODO: convert status to Long
        List<ConfigView> configViewList = configViewRepository.findByStatus(1L);
        return configViewList.stream().map(this::convertToConfigViewResponseDTO).toList();
    }

    @Override
    public ConfigViewResponseDTO getConfigViewById(Long id) {
        ConfigView configView = configViewRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("config-view.not.found")));
        return convertToConfigViewResponseDTO(configView);

    }

    @Override
    public ConfigViewResponseDTO getConfigViewByName(String name) {
        return convertToConfigViewResponseDTO(configViewRepository.findByViewName(name));
    }


    @Override
    public ConfigViewResponseDTO createConfigView(CreateConfigViewRequest configView) {
        ConfigView newConfigView = ConfigView.builder()
                .viewName(configView.getViewName())
                .viewPath(configView.getViewPath())
                .apiPath(configView.getApiPath())
                .roleId(configView.getRoleId())
                .status(configView.getStatus())
                .build();

        return convertToConfigViewResponseDTO(configViewRepository.save(newConfigView));
    }

    @Override
    public ConfigViewResponseDTO updateConfigView(Long id, UpdateConfigViewRequest configView) {
        ConfigView updateConfigView = configViewRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("config-view.not.found")));
        updateConfigView.setViewName(configView.getViewName());
        updateConfigView.setViewPath(configView.getViewPath());
        updateConfigView.setApiPath(configView.getApiPath());
        updateConfigView.setRoleId(configView.getRoleId());
        updateConfigView.setStatus(configView.getStatus());
        return convertToConfigViewResponseDTO(configViewRepository.save(updateConfigView));
    }

    @Override
    public ConfigViewResponseDTO updateConfigViewStatus(Long id, String status) {
        ConfigView updateConfigView = configViewRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("config-view.not.found")));
        //TODO: set status
        updateConfigView.setStatus(1L);
        return convertToConfigViewResponseDTO(configViewRepository.save(updateConfigView));
    }

    @Override
    public void deleteConfigView(Long id) {
        ConfigView configView = configViewRepository.findById(id).orElseThrow(() -> new RuntimeException(Translator.toLocate("config-view.not.found")));
        configViewRepository.delete(configView);

    }
}
