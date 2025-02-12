package com.duongw.workforceservice.repository.search.specifications;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.workforceservice.model.entity.WorkType;
import org.springframework.data.jpa.domain.Specification;

public class WorkTypeSpecification extends BaseSpecification<WorkType> {


    public WorkTypeSpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }


    public static Specification<WorkType> searchByMultipleFields(String workTypeCode, String workTypeName) {

        Specification<WorkType> workTypeSpecification = Specification.where(null);

        // default search with like operator

        if(workTypeCode!= null && !workTypeCode.isEmpty()){
            workTypeSpecification = workTypeSpecification.and(new WorkTypeSpecification(
                    new SearchCriteria("workTypeCode", ":", workTypeCode)));
        }

        if(workTypeName!= null && !workTypeName.isEmpty()){
            workTypeSpecification = workTypeSpecification.and(new WorkTypeSpecification(
                    new SearchCriteria("workTypeName", ":", workTypeName)));
        }

        return workTypeSpecification;
    }
}
