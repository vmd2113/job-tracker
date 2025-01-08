package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.model.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<Files, Long> {

    Files findByBusinessCode(String businessCode);
    Files findByBusinessId(Long businessId);

    void deleteByBusinessId(Long businessId);
    void deleteByBusinessCode(String businessCode);

}
