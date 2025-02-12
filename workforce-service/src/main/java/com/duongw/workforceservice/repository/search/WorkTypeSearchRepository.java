package com.duongw.workforceservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.entity.WorkType;
import com.duongw.workforceservice.repository.WorkTypeRepository;
import com.duongw.workforceservice.repository.search.specifications.WorkTypeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class WorkTypeSearchRepository {

    private final WorkTypeRepository workTypeRepository;

    public WorkTypeSearchRepository(WorkTypeRepository workTypeRepository) {
        this.workTypeRepository = workTypeRepository;
    }

    public PageResponse<WorkTypeResponseDTO> searchWorkType(String workTypeCode, String workTypeName, int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        int pageAdjust = pageNo > 1 ? pageNo - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageAdjust, pageSize, sort);
        Page<WorkType> workTypeResponsePage = workTypeRepository.findAll(
                WorkTypeSpecification.searchByMultipleFields(workTypeCode, workTypeName),
                pageRequest);

        Page<WorkTypeResponseDTO> workTypeResponseDTOPage = workTypeResponsePage.map(workType -> WorkTypeResponseDTO.builder()
                .workTypeId(workType.getWorkTypeId())
                .workTypeCode(workType.getWorkTypeCode())
                .workTypeName(workType.getWorkTypeName())
                .priorityId(workType.getPriorityId())
                .processTime(workType.getProcessTime())
                .status(workType.getStatus())
                .build());

        return PageResponseConverter.convertFromPage(workTypeResponseDTOPage);

    }

}
