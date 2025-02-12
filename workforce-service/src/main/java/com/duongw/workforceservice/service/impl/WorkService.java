package com.duongw.workforceservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.works.CreateWorkRequest;
import com.duongw.workforceservice.model.dto.resquest.works.UpdateWorkRequest;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.model.entity.Works;
import com.duongw.workforceservice.repository.WorkRepository;
import com.duongw.workforceservice.repository.WorkTypeRepository;
import com.duongw.workforceservice.repository.search.WorkSearchRepository;
import com.duongw.workforceservice.service.IWorkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class WorkService implements IWorkService {

    private final WorkRepository workRepository;
    private final WorkTypeRepository workTypeRepository;
    private final WorkSearchRepository workSearchRepository;

    public WorkService(WorkRepository workRepository, WorkTypeRepository workTypeRepository, WorkSearchRepository workSearchRepository) {
        this.workRepository = workRepository;
        this.workTypeRepository = workTypeRepository;
        this.workSearchRepository = workSearchRepository;
    }

    private Works converToWorks(WorkResponseDTO workResponseDTO) {
        Works works = new Works();
        works.setWorkCode(workResponseDTO.getWorkCode());
        works.setWorkContent(workResponseDTO.getWorkContent());

        WorkType workType = workTypeRepository.findById(workResponseDTO.getWorkTypeId()).orElse(null);
        works.setWorkType(workType);

        works.setPriorityId(workResponseDTO.getPriorityId());
        works.setStatus(workResponseDTO.getStatus());
        works.setStartTime(workResponseDTO.getStartTime());
        works.setEndTime(workResponseDTO.getEndTime());
        works.setFinishTime(workResponseDTO.getFinishTime());
        works.setAssignedUserId(workResponseDTO.getAssignedUserId());
        return works;
    }


    private WorkResponseDTO converToWorkResponseDTO(Works works) {
        return WorkResponseDTO.builder()
                .worksId(works.getWorksId())
                .workCode(works.getWorkCode())
                .workContent(works.getWorkContent())
                .workTypeId(works.getWorkType().getWorkTypeId())
                .priorityId(works.getPriorityId())
                .status(works.getStatus())
                .startTime(works.getStartTime())
                .endTime(works.getEndTime())
                .finishTime(works.getFinishTime())
                .assignedUserId(works.getAssignedUserId())
                .build();
    }

    @Override
    public List<WorkResponseDTO> getAllWork() {
        List<Works> worksList = workRepository.findAll();
        return worksList.stream().map(this::converToWorkResponseDTO).toList();
    }

    @Override
    public WorkResponseDTO getWorkById(Long id) {
        Works works = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.get-by-id.not-found")));
        return converToWorkResponseDTO(works);
    }

    @Override
    public WorkResponseDTO createWork(CreateWorkRequest work) {
        Works works = new Works();
        works.setWorkCode(work.getWorkCode());
        works.setWorkContent(work.getWorkContent());
        works.setWorkType(workTypeRepository.findById(work.getWorkTypeId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.create.not-found"))));
        works.setPriorityId(work.getPriorityId());
        works.setStatus(work.getStatus());
        works.setStartTime(work.getStartTime());
        works.setEndTime(work.getEndTime());
        works.setFinishTime(null);
        works.setAssignedUserId(work.getAssignedUserId());
        workRepository.save(works);
        return converToWorkResponseDTO(works);
    }

    @Override
    public WorkResponseDTO updateWork(Long id, UpdateWorkRequest work) {
        Works works = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.update.not-found")));
        works.setWorkCode(work.getWorkCode());
        works.setWorkContent(work.getWorkContent());
        works.setWorkType(workTypeRepository.findById(work.getWorkTypeId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.update.not-found"))));
        works.setPriorityId(work.getPriorityId());
        works.setStatus(work.getStatus());
        works.setStartTime(work.getStartTime());
        works.setEndTime(work.getEndTime());
        works.setFinishTime(work.getFinishTime());
        works.setAssignedUserId(work.getAssignedUserId());
        workRepository.save(works);
        return converToWorkResponseDTO(works);

    }

    @Override
    public void deleteWorkById(Long id) {
        Works works = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.delete.not-found")));
        workRepository.delete(works);

    }

    @Override
    public PageResponse<WorkResponseDTO> searchWorks(String workCode, String workContent, Long workTypeId, Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId, int pageNo, int pageSize, String sortBy, String sortDirection) {
        return workSearchRepository.searchWorks(workCode, workContent, workTypeId, priorityId, status, startTime, endTime, finishTime, assignedUserId, pageNo, pageSize, sortBy, sortDirection);
    }


    //TODO: complete search

}
