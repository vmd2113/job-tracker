package com.duongw.workforceservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.workforceservice.model.dto.response.works.WorkResponseDTO;
import com.duongw.workforceservice.model.entity.Works;
import com.duongw.workforceservice.repository.WorkRepository;
import com.duongw.workforceservice.repository.search.specifications.WorksSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class WorkSearchRepository {

    private final WorkRepository workRepository;

    @Autowired
    public WorkSearchRepository(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public PageResponse<WorkResponseDTO> searchWorks(String workCode, String workContent, Long workTypeId, Long priorityId, Long status, String startTime, String endTime, String finishTime, Long assignedUserId, int pageNo, int pageSize, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        int pageAdjust = pageNo > 1 ? pageNo - 1 : 0;

        PageRequest pageRequest = PageRequest.of(pageAdjust, pageSize, sort);


        Page<Works> worksPage = workRepository.findAll(
                WorksSpecification.searchByMultipleFields(workCode, workContent, workTypeId, priorityId, status, startTime, endTime, finishTime, assignedUserId),
                pageRequest
        );

        Page<WorkResponseDTO> workResponsePage = worksPage.map(works -> WorkResponseDTO.builder()
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
                .build());

        return PageResponseConverter.convertFromPage(workResponsePage);

    }


}
