package com.duongw.workforceservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.workconfig.WorkConfigResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.workconfig.CreateWorkConfigRequest;
import com.duongw.workforceservice.model.dto.resquest.workconfig.UpdateWorkConfigRequest;

import java.util.List;

public interface IWorkConfigBusinessService {

    WorkConfigResponseDTO getWorkConfigById(Long id);

    List<WorkConfigResponseDTO> getAllWorkConfig();

    WorkConfigResponseDTO createWorkConfig(CreateWorkConfigRequest workConfig);

    WorkConfigResponseDTO updateWorkConfig(Long id, UpdateWorkConfigRequest workConfig);

    void deleteWorkConfigById(Long id);

    public PageResponse<WorkConfigResponseDTO> searchWorkConfig(String workTypeName, Long priorityId, Long oldStatus, Long newStatus, int page, int size, String sortBy, String sortDirection);
}
