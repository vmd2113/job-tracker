package com.duongw.workforceservice.repository;

import com.duongw.workforceservice.model.entity.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Works, Long>, JpaSpecificationExecutor<Works> {

}
