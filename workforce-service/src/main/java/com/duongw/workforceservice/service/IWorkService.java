package com.duongw.workforceservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.works.CreateWorkRequest;
import com.duongw.workforceservice.model.dto.resquest.works.UpdateWorkRequest;

import java.util.List;

public interface IWorkService {

    List<WorkResponseDTO> getAllWork();

    WorkResponseDTO getWorkById(Long id);


    WorkResponseDTO createWork(CreateWorkRequest work);

    WorkResponseDTO updateWork(Long id, UpdateWorkRequest work);

    void deleteWorkById(Long id);


    PageResponse<WorkResponseDTO> searchWorks(String workCode, String workContent, Long workTypeId, Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId, int pageNo, int pageSize, String sortBy, String sortDirection);
}
