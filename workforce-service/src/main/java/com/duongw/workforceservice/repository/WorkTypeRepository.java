package com.duongw.workforceservice.repository;

import com.duongw.workforceservice.model.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, Long>, JpaSpecificationExecutor<WorkType> {
    WorkType findByWorkTypeCode(String workTypeCode);
    WorkType findByWorkTypeName(String workTypeName);
    boolean existsByWorkTypeCode(String workTypeCode);
    boolean existsByWorkTypeName(String workTypeName);

}
