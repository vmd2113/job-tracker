package com.duongw.workforceservice.repository;

import com.duongw.workforceservice.model.entity.WorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Long> {
}
