package com.duongw.workforceservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.workconfig.WorkConfigResponseDTO;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.workconfig.CreateWorkConfigRequest;
import com.duongw.workforceservice.model.dto.resquest.workconfig.UpdateWorkConfigRequest;
import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.repository.WorkConfigBusinessRepository;
import com.duongw.workforceservice.repository.search.WorkConfigSearchRepository;
import com.duongw.workforceservice.service.IWorkConfigBusinessService;
import com.duongw.workforceservice.service.IWorkTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j


public class WorkConfigBusinessService implements IWorkConfigBusinessService {

    private final WorkConfigBusinessRepository workConfigBusinessRepository;
    private final IWorkTypeService workTypeService;
    private final WorkConfigSearchRepository workConfigSearchRepository;

    public WorkConfigBusinessService(WorkConfigBusinessRepository workConfigBusinessRepository, IWorkTypeService workTypeService, WorkConfigSearchRepository workConfigSearchRepository) {
        this.workConfigBusinessRepository = workConfigBusinessRepository;
        this.workTypeService = workTypeService;
        this.workConfigSearchRepository = workConfigSearchRepository;
    }

    private WorkConfigResponseDTO converToWorkConfigResponseDTO(WorkConfigBusiness workConfigBusiness) {
        WorkTypeResponseDTO workTypeResponseDTO = workTypeService.getWorkTypeById(workConfigBusiness.getWorkType().getWorkTypeId());
        return WorkConfigResponseDTO.builder()
                .workConfigId(workConfigBusiness.getWorkConfigId())
                .workType(workTypeResponseDTO)
                .priorityId(workConfigBusiness.getPriorityId())
                .oldStatus(workConfigBusiness.getOldStatus())
                .newStatus(workConfigBusiness.getNewStatus())
                .build();

    }

    private WorkConfigBusiness converToWorkConfigBusiness(WorkConfigResponseDTO workConfigResponseDTO) {
        WorkConfigBusiness workConfigBusiness = new WorkConfigBusiness();
        workConfigBusiness.setPriorityId(workConfigResponseDTO.getPriorityId());
        workConfigBusiness.setOldStatus(workConfigResponseDTO.getOldStatus());
        workConfigBusiness.setNewStatus(workConfigResponseDTO.getNewStatus());
        return workConfigBusiness;
    }

    @Override
    public WorkConfigResponseDTO getWorkConfigById(Long id) {
        WorkConfigBusiness workConfigBusiness = workConfigBusinessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-config.get-by-id.not-found")));
        return converToWorkConfigResponseDTO(workConfigBusiness);
    }

    @Override
    public List<WorkConfigResponseDTO> getAllWorkConfig() {
        List<WorkConfigBusiness> workConfigBusinessList = workConfigBusinessRepository.findAll();
        return workConfigBusinessList.stream().map(this::converToWorkConfigResponseDTO).toList();
    }


    @Override
    public WorkConfigResponseDTO createWorkConfig(CreateWorkConfigRequest workConfig) {
        WorkConfigBusiness workConfigBusiness = new WorkConfigBusiness();

        WorkType workType = workTypeService.findById(workConfig.getWorkTypeId());
        workConfigBusiness.setWorkType(workType);
        workConfigBusiness.setPriorityId(workConfig.getPriorityId());
        workConfigBusiness.setOldStatus(workConfig.getOldStatus());
        workConfigBusiness.setNewStatus(workConfig.getNewStatus());
        workConfigBusinessRepository.save(workConfigBusiness);
        return converToWorkConfigResponseDTO(workConfigBusiness);
    }

    @Override
    public WorkConfigResponseDTO updateWorkConfig(Long id, UpdateWorkConfigRequest workConfig) {
        WorkConfigBusiness workConfigBusiness = workConfigBusinessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-config.update.not-found")));
        workConfigBusiness.setWorkType(workTypeService.findById(workConfig.getWorkTypeId()));
        workConfigBusiness.setPriorityId(workConfig.getPriorityId());
        workConfigBusiness.setOldStatus(workConfig.getOldStatus());
        workConfigBusiness.setNewStatus(workConfig.getNewStatus());
        workConfigBusinessRepository.save(workConfigBusiness);
        return converToWorkConfigResponseDTO(workConfigBusiness);
    }

    @Override
    public void deleteWorkConfigById(Long id) {

        WorkConfigBusiness workConfigBusiness = workConfigBusinessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-config.delete.not-found")));
        workConfigBusinessRepository.delete(workConfigBusiness);

    }

    @Override
    public PageResponse<WorkConfigResponseDTO> searchWorkConfig(String workTypeName, Long priorityId, Long oldStatus, Long newStatus, int page, int size, String sortBy, String sortDirection) {
        return workConfigSearchRepository.searchWorkConfig(workTypeName, priorityId, oldStatus, newStatus, page, size, sortBy, sortDirection);
    }

}
