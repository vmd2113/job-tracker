package com.duongw.workforceservice.service;

import com.duongw.workforceservice.model.dto.response.workhistory.WorkHistoryResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.workhistory.CreateWorkHistory;
import com.duongw.workforceservice.model.dto.resquest.workhistory.UpdateWorkHistory;

import java.util.List;

public interface IWorkHistoryService {

    List<WorkHistoryResponseDTO> getAllWorkHistory();

    WorkHistoryResponseDTO getWorkHistoryById(Long id);

    WorkHistoryResponseDTO createWorkHistory(CreateWorkHistory workHistory);

    WorkHistoryResponseDTO updateWorkHistory(Long id, UpdateWorkHistory workHistory);

    void deleteWorkHistoryById(Long id);

    void deleteListWorkHistory(List<Long> ids);

    List<WorkHistoryResponseDTO> searchWorkHistory(int page, int size, String workCode, String workContent, Long workTypeId, Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId, String sortBy, String sortDirection);
}
