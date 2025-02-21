package com.duongw.workforceservice.repository;

import com.duongw.workforceservice.model.entity.WorkConfigBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkConfigBusinessRepository extends JpaRepository<WorkConfigBusiness, Long> , JpaSpecificationExecutor<WorkConfigBusiness> {
}
