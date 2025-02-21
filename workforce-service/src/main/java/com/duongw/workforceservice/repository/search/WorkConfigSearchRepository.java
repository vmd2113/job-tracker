package com.duongw.workforceservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.workforceservice.client.CommonClientWO;
import com.duongw.workforceservice.model.dto.response.workconfig.WorkConfigResponseDTO;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.repository.WorkConfigBusinessRepository;
import com.duongw.workforceservice.repository.search.specifications.WorkConfigSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository

@Transactional(readOnly = true)
public class WorkConfigSearchRepository {

    private final WorkConfigBusinessRepository workConfigBusinessRepository;
    private final CommonClientWO commonClientWO;

    public WorkConfigSearchRepository(WorkConfigBusinessRepository workConfigBusinessRepository, CommonClientWO commonClientWO) {
        this.workConfigBusinessRepository = workConfigBusinessRepository;
        this.commonClientWO = commonClientWO;
    }

    public PageResponse<WorkConfigResponseDTO> searchWorkConfig(
            String workTypeName,
            Long priorityId,
            Long oldStatus,
            Long newStatus,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        // Validate và xử lý sorting
        Sort sort = createSort(sortBy, sortDirection);

        // Xử lý pagination
        int pageAdjust = Math.max(0, page - 1);
        PageRequest pageRequest = PageRequest.of(pageAdjust, size, sort);

        // Thực hiện tìm kiếm với specification
        Page<WorkConfigBusiness> workConfigBusinesses = workConfigBusinessRepository.findAll(
                WorkConfigSpecification.searchByMultipleFields(
                        workTypeName,
                        priorityId,
                        oldStatus,
                        newStatus
                ),
                pageRequest
        );

        // Convert kết quả sang DTO
        Page<WorkConfigResponseDTO> responseDTOPage = workConfigBusinesses.map(this::convertToDTO);

        return PageResponseConverter.convertFromPage(responseDTOPage);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            return Sort.by(direction, sortBy);
        } catch (IllegalArgumentException e) {
            // Fallback to default sorting if invalid parameters
            return Sort.by(Sort.Direction.DESC, "workConfigId");
        }
    }

    private WorkConfigResponseDTO convertToDTO(WorkConfigBusiness entity) {
        return WorkConfigResponseDTO.builder()
                .workConfigId(entity.getWorkConfigId())
                .workTypeName(entity.getWorkType().getWorkTypeName())
                .workTypeId(entity.getWorkType().getWorkTypeId())
                .priorityId(entity.getPriorityId())
                .priorityName(commonClientWO.getItemById(entity.getPriorityId()).getItemName())
                .oldStatusId(entity.getOldStatus())
                .oldStatusName(commonClientWO.getItemById(entity.getOldStatus()).getItemName())
                .newStatusId(entity.getNewStatus())
                .newStatusName(commonClientWO.getItemById(entity.getNewStatus()).getItemName())
                .build();
    }

    private WorkTypeResponseDTO convertWorkTypeToDTO(WorkType workType) {
        return WorkTypeResponseDTO.builder()
                .workTypeId(workType.getWorkTypeId())
                .workTypeCode(workType.getWorkTypeCode())
                .workTypeName(workType.getWorkTypeName())
                .priorityId(workType.getPriorityId())
                .processTime(workType.getProcessTime())
                .status(workType.getStatus())
                .build();
    }
}