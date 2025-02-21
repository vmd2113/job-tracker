package com.duongw.workforceservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.client.CommonClientWO;
import com.duongw.workforceservice.model.dto.response.item.ItemResponseDTO;
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
import com.duongw.workforceservice.validator.WorkConfigValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j


public class WorkConfigBusinessService implements IWorkConfigBusinessService {

    public static final Long CATEGORY_STATUS = 2L;
    public static final Long CATEGORY_PRIORITY = 1L;

    private final WorkConfigBusinessRepository workConfigBusinessRepository;
    private final WorkConfigValidator workConfigValidator;
    private final IWorkTypeService workTypeService;
    private final CommonClientWO commonClientWO;

    private final WorkConfigSearchRepository workConfigSearchRepository;

    public WorkConfigBusinessService(WorkConfigBusinessRepository workConfigBusinessRepository, WorkConfigValidator workConfigValidator, IWorkTypeService workTypeService, CommonClientWO commonClientWO, WorkConfigSearchRepository workConfigSearchRepository) {
        this.workConfigBusinessRepository = workConfigBusinessRepository;
        this.workConfigValidator = workConfigValidator;
        this.workTypeService = workTypeService;
        this.commonClientWO = commonClientWO;
        this.workConfigSearchRepository = workConfigSearchRepository;
    }

    private WorkConfigResponseDTO converToWorkConfigResponseDTO(WorkConfigBusiness workConfigBusiness) {
        WorkTypeResponseDTO workTypeResponseDTO = workTypeService.getWorkTypeById(workConfigBusiness.getWorkType().getWorkTypeId());
        return WorkConfigResponseDTO.builder()
                .workConfigId(workConfigBusiness.getWorkConfigId())
                .workTypeName(workTypeResponseDTO.getWorkTypeName())
                .workTypeId(workTypeResponseDTO.getWorkTypeId())

                .priorityId(workConfigBusiness.getPriorityId())
                .priorityName(findItemByIdAndCategoryId(workConfigBusiness.getPriorityId(), CATEGORY_PRIORITY).getItemName())
                .oldStatusId(workConfigBusiness.getOldStatus())
                .oldStatusName(findItemByIdAndCategoryId(workConfigBusiness.getOldStatus(), CATEGORY_STATUS).getItemName())
                .newStatusId(workConfigBusiness.getNewStatus())
                .newStatusName(findItemByIdAndCategoryId(workConfigBusiness.getNewStatus(), CATEGORY_STATUS).getItemName())
                .build();

    }

    private WorkConfigBusiness converToWorkConfigBusiness(WorkConfigResponseDTO workConfigResponseDTO) {
        WorkConfigBusiness workConfigBusiness = new WorkConfigBusiness();
        workConfigBusiness.setPriorityId(workConfigResponseDTO.getPriorityId());
        workConfigBusiness.setOldStatus(workConfigResponseDTO.getOldStatusId());
        workConfigBusiness.setNewStatus(workConfigResponseDTO.getNewStatusId());
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

        workConfigValidator.validateCreateWorkConfig(workConfig);
        WorkConfigBusiness workConfigBusiness = new WorkConfigBusiness();

        WorkType workType = workTypeService.findById(workConfig.getWorkTypeId());
        workConfigBusiness.setWorkType(workType);

        try {


            workConfigBusiness.setPriorityId(findItemByIdAndCategoryId(workConfig.getPriorityId(), CATEGORY_PRIORITY).getItemId());
            workConfigBusiness.setOldStatus(findItemByIdAndCategoryId(workConfig.getOldStatusId(), CATEGORY_STATUS).getItemId());
            workConfigBusiness.setNewStatus(findItemByIdAndCategoryId(workConfig.getNewStatusId(), CATEGORY_STATUS).getItemId());

            WorkConfigBusiness saveWorkConfig = workConfigBusinessRepository.save(workConfigBusiness);
            return converToWorkConfigResponseDTO(saveWorkConfig);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu");
        }

    }

    @Override
    public WorkConfigResponseDTO updateWorkConfig(Long id, UpdateWorkConfigRequest workConfig) {
        workConfigValidator.validateUpdateWorkConfig(id, workConfig);
        WorkConfigBusiness workConfigBusiness = workConfigBusinessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-config.update.not-found")));
        workConfigBusiness.setWorkType(workTypeService.findById(workConfig.getWorkTypeId()));
        try {

            workConfigBusiness.setPriorityId(findItemByIdAndCategoryId(workConfig.getPriorityId(), CATEGORY_PRIORITY).getItemId());
            workConfigBusiness.setOldStatus(findItemByIdAndCategoryId(workConfig.getOldStatusId(), CATEGORY_STATUS).getItemId());
            workConfigBusiness.setNewStatus(findItemByIdAndCategoryId(workConfig.getNewStatusId(), CATEGORY_STATUS).getItemId());
            WorkConfigBusiness saveWorkConfig = workConfigBusinessRepository.save(workConfigBusiness);
            return converToWorkConfigResponseDTO(saveWorkConfig);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu");
        }

    }

    @Override
    public void deleteWorkConfigById(Long id) {

        WorkConfigBusiness workConfigBusiness = workConfigBusinessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work-config.delete.not-found")));
        workConfigBusinessRepository.delete(workConfigBusiness);

    }

    @Override
    public void deleteListWorkConfig(List<Long> ids) {
        for (Long id : ids) {
            deleteWorkConfigById(id);
        }
    }

    @Override
    public PageResponse<WorkConfigResponseDTO> searchWorkConfig(String workTypeName, Long priorityId, Long oldStatus, Long newStatus, int page, int size, String sortBy, String sortDirection) {
        return workConfigSearchRepository.searchWorkConfig(workTypeName, priorityId, oldStatus, newStatus, page, size, sortBy, sortDirection);
    }

    private ItemResponseDTO findItemByIdAndCategoryId(Long itemId, Long categoryId) {
        try {
            return commonClientWO.getItemByIdAndCategoryId(itemId, categoryId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu");
        }
    }

}
