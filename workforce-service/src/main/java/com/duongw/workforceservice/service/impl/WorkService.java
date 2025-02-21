package com.duongw.workforceservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.client.CommonClientWO;
import com.duongw.workforceservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.works.CreateWorkRequest;
import com.duongw.workforceservice.model.dto.resquest.works.UpdateWorkRequest;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.model.entity.Works;
import com.duongw.workforceservice.repository.WorkRepository;
import com.duongw.workforceservice.repository.WorkTypeRepository;
import com.duongw.workforceservice.repository.search.WorkSearchRepository;
import com.duongw.workforceservice.service.IWorkService;
import com.duongw.workforceservice.validator.WorksValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service

public class WorkService implements IWorkService {


    public static final Long CATEGORY_PRIORITY = 1L;
    public static final Long CATEGORY_STATUS = 2L;

    private final WorkRepository workRepository;
    private final WorkTypeRepository workTypeRepository;
    private final WorkSearchRepository workSearchRepository;
    private final WorksValidator worksValidator;

    private final CommonClientWO commonClientWO;


    @PersistenceContext
    private EntityManager entityManager;


    public WorkService(WorkRepository workRepository, WorkTypeRepository workTypeRepository, WorkSearchRepository workSearchRepository, WorksValidator worksValidator, CommonClientWO commonClientWO) {
        this.workRepository = workRepository;
        this.workTypeRepository = workTypeRepository;
        this.workSearchRepository = workSearchRepository;
        this.worksValidator = worksValidator;
        this.commonClientWO = commonClientWO;
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
        ItemResponseDTO item = findItemByIdAndCategoryId(works.getPriorityId(), CATEGORY_PRIORITY);
        ItemResponseDTO status = findItemByIdAndCategoryId(works.getStatus(), CATEGORY_STATUS);



        return WorkResponseDTO.builder()
                .worksId(works.getWorksId())
                .workCode(works.getWorkCode())
                .workContent(works.getWorkContent())
                .workTypeId(works.getWorkType().getWorkTypeId())
                .workTypeName(works.getWorkType().getWorkTypeName())
                .priorityId(works.getPriorityId())
                .priorityName(item.getItemName())
                .status(works.getStatus())
                .statusName(status.getItemName())
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

    @Transactional
    public WorkResponseDTO createWork(CreateWorkRequest work) {

        // Validate work
        worksValidator.validateCreateWork(work);
        // Generate work code based on work type
        String workCode = generateWorkCode(work.getWorkTypeId());

        Works works = new Works();
        works.setWorkCode(workCode); // Use generated code instead of request workCode
        works.setWorkContent(work.getWorkContent());

        // Get and set work type
        WorkType workType = workTypeRepository.findById(work.getWorkTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.create.not-found")));
        works.setWorkType(workType);

        works.setPriorityId(work.getPriorityId());

        works.setStatus(1L); // chờ tiếp nhận

        LocalDateTime startTime = work.getStartTime();
        works.setStartTime(work.getStartTime());

        float processTimeInHours = workType.getProcessTime();

        // Convert processTime to hours and minutes
        long fullHours = (long) processTimeInHours;
        long minutes = Math.round((processTimeInHours - fullHours) * 60);

        LocalDateTime endTime = startTime
                .plusHours(fullHours)
                .plusMinutes(minutes);

        works.setEndTime(endTime);

        works.setFinishTime(null);
        works.setAssignedUserId(work.getAssignedUserId());

        workRepository.save(works);
        return converToWorkResponseDTO(works);
    }

    @Override
    public WorkResponseDTO updateWork(Long id, UpdateWorkRequest work) {

        worksValidator.validateUpdateWork(work);
        Works works = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.update.not-found")));


        works.setWorkContent(work.getWorkContent());

        works.setWorkType(workTypeRepository.findById(work.getWorkTypeId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.update.not-found"))));
        works.setPriorityId(work.getPriorityId());

        works.setStatus(work.getStatus());
        works.setStartTime(work.getStartTime());

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
    public void deleteListWorks(List<Long> ids){
        for(Long id: ids){
            deleteWorkById(id);

        }
    }


    @Override
    public PageResponse<WorkResponseDTO> searchWorks(String workCode, String workContent, Long workTypeId, Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId, int pageNo, int pageSize, String sortBy, String sortDirection) {
        return workSearchRepository.searchWorks(workCode, workContent, workTypeId, priorityId, status, startTime, endTime, finishTime, assignedUserId, pageNo, pageSize, sortBy, sortDirection);
    }



    private String generateWorkCode(Long workTypeId) {
        // Get work type code
        WorkType workType = workTypeRepository.findById(workTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("work.create.worktype-not-found")));

        // Get next sequence value
        Query query = entityManager.createNativeQuery("SELECT WFM.WO_SEQ.NEXTVAL FROM DUAL");
        Long sequenceValue = ((Number) query.getSingleResult()).longValue();

        // Format: WO_[WO_TYPE_CODE]_[SEQUENCE]
        return String.format("WO_%s_%03d",
                workType.getWorkTypeCode(),
                sequenceValue);
    }

    private ItemResponseDTO findItemByIdAndCategoryId(Long itemId, Long categoryId) {
        try {
            return commonClientWO.getItemByIdAndCategoryId(itemId, categoryId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu");
        }
    }




}
