package com.duongw.workforceservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.worktype.CreateWorkTypeRequest;
import com.duongw.workforceservice.model.dto.resquest.worktype.UpdateWorkTypeRequest;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.repository.WorkTypeRepository;
import com.duongw.workforceservice.repository.search.WorkTypeSearchRepository;
import com.duongw.workforceservice.service.IWorkTypeService;
import com.duongw.workforceservice.validator.WorkTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WorkTypeService implements IWorkTypeService {

    private final WorkTypeRepository workTypeRepository;
    private final WorkTypeSearchRepository workTypeSearchRepository;
    private final WorkTypeValidator workTypeValidator;

    private WorkType convertToWorkType(WorkTypeResponseDTO workTypeResponseDTO) {
        WorkType workType = new WorkType();
        workType.setWorkTypeId(workTypeResponseDTO.getWorkTypeId());
        workType.setWorkTypeCode(workTypeResponseDTO.getWorkTypeCode());
        workType.setWorkTypeName(workTypeResponseDTO.getWorkTypeName());
        workType.setPriorityId(workTypeResponseDTO.getPriorityId());
        workType.setProcessTime(workTypeResponseDTO.getProcessTime());
        workType.setStatus(workTypeResponseDTO.getStatus());
        return workType;
    }

    private WorkTypeResponseDTO convertToWorkTypeResponseDTO(WorkType workType) {
        return WorkTypeResponseDTO.builder()
                .workTypeId(workType.getWorkTypeId())
                .workTypeCode(workType.getWorkTypeCode())
                .workTypeName(workType.getWorkTypeName())
                .priorityId(workType.getPriorityId())
                .processTime(workType.getProcessTime())
                .status(workType.getStatus())
                .build();
    }


    @Autowired
    public WorkTypeService(WorkTypeRepository workTypeRepository, WorkTypeSearchRepository workTypeSearchRepository, WorkTypeValidator workTypeValidator) {
        this.workTypeRepository = workTypeRepository;
        this.workTypeSearchRepository = workTypeSearchRepository;
        this.workTypeValidator = workTypeValidator;
    }

    @Override
    public WorkType findById(long id) {
        return workTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-type.not-found")));
    }

    @Override
    public List<WorkTypeResponseDTO> getAllWorkType() {
        List<WorkType> workTypes = workTypeRepository.findAll();
        return workTypes.stream().map(this::convertToWorkTypeResponseDTO).toList();
    }

    @Override
    public WorkTypeResponseDTO getWorkTypeById(long id) {
        WorkType workType = workTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-type.not-found")));
        return convertToWorkTypeResponseDTO(workType);
    }

    @Override
    public WorkTypeResponseDTO createWorkType(CreateWorkTypeRequest createWorkTypeRequest) {

        workTypeValidator.validateCreateWorkType(createWorkTypeRequest);
        WorkType workType = new WorkType();
        workType.setWorkTypeCode(createWorkTypeRequest.getWorkTypeCode());
        workType.setWorkTypeName(createWorkTypeRequest.getWorkTypeName());
        workType.setPriorityId(createWorkTypeRequest.getPriorityId());
        workType.setProcessTime(createWorkTypeRequest.getProcessTime());
        workType.setStatus(createWorkTypeRequest.getStatus());
        workType = workTypeRepository.save(workType);
        return convertToWorkTypeResponseDTO(workType);
    }

    @Override
    public WorkTypeResponseDTO updateWorkType(long id, UpdateWorkTypeRequest updateWorkTypeRequest) {

        workTypeValidator.validateUpdateWorkType(id, updateWorkTypeRequest);
        WorkType workType = workTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("WorkType not found"));
        workType.setWorkTypeCode(updateWorkTypeRequest.getWorkTypeCode());
        workType.setWorkTypeName(updateWorkTypeRequest.getWorkTypeName());
        workType.setPriorityId(updateWorkTypeRequest.getPriorityId());
        workType.setProcessTime(updateWorkTypeRequest.getProcessTime());
        workType.setStatus(updateWorkTypeRequest.getStatus());
        return convertToWorkTypeResponseDTO(workTypeRepository.save(workType));
    }

    @Override
    public WorkTypeResponseDTO changeStatusWorkType(long id, String status) {
        WorkType workType = workTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("WorkType not found"));
        //TODO: set status
        workType.setStatus(1L);
        return convertToWorkTypeResponseDTO(workTypeRepository.save(workType));
    }

    @Override
    public void deleteWorkTypeById(Long id) {
        WorkType workType = workTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("WorkType not found"));
        workTypeRepository.delete(workType);

    }

    @Override
    public void deleteListWorkTypes(List<Long> ids) {
        for (Long id : ids) {
            deleteWorkTypeById(id);
        }
    }

    @Override
    public PageResponse<WorkTypeResponseDTO> searchWorkType(String workTypeCode, String workTypeName, int pageNo, int pageSize, String sortBy, String sortDirection) {
        return workTypeSearchRepository.searchWorkType(workTypeCode, workTypeName, pageNo, pageSize, sortBy, sortDirection);
    }
}
