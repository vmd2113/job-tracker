package com.duongw.workforceservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.workforceservice.model.dto.response.workhistory.WorkHistoryResponseDTO;
import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.workhistory.CreateWorkHistory;
import com.duongw.workforceservice.model.dto.resquest.workhistory.UpdateWorkHistory;
import com.duongw.workforceservice.model.entity.WorkHistory;
import com.duongw.workforceservice.model.entity.Works;
import com.duongw.workforceservice.repository.WorkHistoryRepository;
import com.duongw.workforceservice.repository.WorkRepository;
import com.duongw.workforceservice.service.IWorkHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class WorkHistoryService implements IWorkHistoryService {

    private final WorkHistoryRepository workHistoryRepository;
    private final WorkRepository workRepository;

    @Autowired
    public WorkHistoryService(WorkHistoryRepository workHistoryRepository, WorkRepository workRepository) {
        this.workHistoryRepository = workHistoryRepository;
        this.workRepository = workRepository;
    }

    private WorkHistoryResponseDTO converToWorkHistoryResponseDTO(WorkHistory workHistory) {
        Works works = workRepository.findById(workHistory.getWork().getWorksId()).orElse(null);
        WorkResponseDTO workResponseDTO = new WorkResponseDTO();
        workResponseDTO.setWorksId(works.getWorksId());
        workResponseDTO.setWorkCode(works.getWorkCode());
        workResponseDTO.setWorkContent(works.getWorkContent());
        workResponseDTO.setWorkTypeId(works.getWorkType().getWorkTypeId());

        workResponseDTO.setPriorityId(works.getPriorityId());
        workResponseDTO.setStatus(works.getStatus());
        workResponseDTO.setStartTime(works.getStartTime());
        workResponseDTO.setEndTime(works.getEndTime());
        workResponseDTO.setFinishTime(works.getFinishTime());
        workResponseDTO.setAssignedUserId(works.getAssignedUserId());

        WorkHistoryResponseDTO workHistoryResponseDTO = new WorkHistoryResponseDTO();
        workHistoryResponseDTO.setWork(workResponseDTO);
        workHistoryResponseDTO.setWorkCode(workHistory.getWorkCode());
        workHistoryResponseDTO.setWorkContent(workHistory.getWorkContent());
        return workHistoryResponseDTO;

    }

    private WorkHistory converToWorkHistory(WorkHistoryResponseDTO workHistoryResponseDTO) {
        Works works = workRepository.findById(workHistoryResponseDTO.getWork().getWorksId()).orElse(null);
        WorkHistory workHistory = new WorkHistory();
        workHistory.setWork(works);
        workHistory.setWorkCode(workHistoryResponseDTO.getWorkCode());
        workHistory.setWorkContent(workHistoryResponseDTO.getWorkContent());
        return workHistory;
    }

    @Override
    public List<WorkHistoryResponseDTO> getAllWorkHistory() {
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll();
        return workHistoryList.stream().map(this::converToWorkHistoryResponseDTO).toList();
    }

    @Override
    public WorkHistoryResponseDTO getWorkHistoryById(Long id) {
        WorkHistory workHistory = workHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-history.get-by-id.not-found")));
        return converToWorkHistoryResponseDTO(workHistory);
    }

    @Override
    public WorkHistoryResponseDTO createWorkHistory(CreateWorkHistory workHistory) {
        WorkHistory workHistory1 = new WorkHistory();
        Works works = workRepository.findById(workHistory.getWorkId()).orElseThrow(()-> new ResourceNotFoundException(Translator.toLocate("works-not-found")));
        workHistory1.setWork(works);
        workHistory1.setWorkCode(workHistory.getWorkCode());
        workHistory1.setWorkContent(workHistory.getWorkContent());
        workHistoryRepository.save(workHistory1);
        return converToWorkHistoryResponseDTO(workHistory1);
    }

    @Override
    public WorkHistoryResponseDTO updateWorkHistory(Long id, UpdateWorkHistory
            workHistory) {
        WorkHistory workHistory1 = workHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-history.update.not-found")));


        workHistory1.setWorkCode(workHistory.getWorkCode());
        workHistory1.setWorkContent(workHistory.getWorkContent());
        workHistoryRepository.save(workHistory1);
        return converToWorkHistoryResponseDTO(workHistory1);
    }

    @Override
    public void deleteWorkHistoryById(Long id) {
        WorkHistory workHistory = workHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-history.delete.not-found")));
        workHistoryRepository.delete(workHistory);

    }

    //TODO: search work history

    @Override
    public List<WorkHistoryResponseDTO> searchWorkHistory(int page, int size, String workCode, String workContent, Long workTypeId, Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId, String sortBy, String sortDirection) {
        return List.of();
    }
}
