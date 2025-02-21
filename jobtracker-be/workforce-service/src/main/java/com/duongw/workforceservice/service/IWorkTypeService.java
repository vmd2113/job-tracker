package com.duongw.workforceservice.service;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.workforceservice.model.dto.response.worktype.WorkTypeResponseDTO;
import com.duongw.workforceservice.model.dto.resquest.worktype.CreateWorkTypeRequest;
import com.duongw.workforceservice.model.dto.resquest.worktype.UpdateWorkTypeRequest;
import com.duongw.workforceservice.model.entity.WorkType;

import java.util.List;

public interface IWorkTypeService {

    WorkType findById(long id);
    List<WorkTypeResponseDTO> getAllWorkType();
    WorkTypeResponseDTO getWorkTypeById(long id);
    WorkTypeResponseDTO createWorkType (CreateWorkTypeRequest createWorkTypeRequest);
    WorkTypeResponseDTO updateWorkType(long id, UpdateWorkTypeRequest updateWorkTypeRequest);
    WorkTypeResponseDTO changeStatusWorkType(long id, String status);
    void deleteWorkTypeById(Long id);

    void deleteListWorkTypes(List<Long> ids);

    PageResponse<WorkTypeResponseDTO> searchWorkType(String workTypeCode, String workTypeName, int pageNo, int pageSize, String sortBy, String sortDirection) ;


}
