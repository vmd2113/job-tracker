package com.duongw.workforceservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.workforceservice.model.dto.response.workconfig.WorkConfigResponseDTO;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import com.duongw.workforceservice.repository.WorkConfigBusinessRepository;
import com.duongw.workforceservice.repository.search.specifications.WorkConfigSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class WorkConfigSearchRepository {

    private final WorkConfigBusinessRepository workConfigBusinessRepository;

    public WorkConfigSearchRepository(WorkConfigBusinessRepository workConfigBusinessRepository) {
        this.workConfigBusinessRepository = workConfigBusinessRepository;
    }

    public PageResponse<WorkConfigResponseDTO> searchWorkConfig(String workTypeName, Long priorityId, Long oldStatus, Long newStatus, int page, int size,  String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        int pageAdjust = page > 1 ? page - 1 : 0;

        PageRequest pageRequest = PageRequest.of(pageAdjust, size, sort);

        Page<WorkConfigBusiness> workConfigBusinesses = workConfigBusinessRepository.findAll(
                WorkConfigSpecification.searchByMultipleFields(workTypeName, priorityId, oldStatus, newStatus),
                pageRequest);


        Page<WorkConfigResponseDTO> workConfigResponseDTOPage = workConfigBusinesses.map(
                workConfigBusiness -> WorkConfigResponseDTO.builder()
                        .workConfigId(workConfigBusiness.getWorkConfigId())
                        .workType(WorkTypeResponseDTO.builder()
                                .workTypeId(workConfigBusiness.getWorkType().getWorkTypeId())
                                .workTypeCode(workConfigBusiness.getWorkType().getWorkTypeCode())
                                .workTypeName(workConfigBusiness.getWorkType().getWorkTypeName())
                                .priorityId(workConfigBusiness.getWorkType().getPriorityId())
                                .processTime(workConfigBusiness.getWorkType().getProcessTime())
                                .status(workConfigBusiness.getWorkType().getStatus())
                                .build())
                        .priorityId(workConfigBusiness.getPriorityId())
                        .oldStatus(workConfigBusiness.getOldStatus())
                        .newStatus(workConfigBusiness.getNewStatus())
                        .build());

        return PageResponseConverter.convertFromPage(workConfigResponseDTOPage);

    }
}
