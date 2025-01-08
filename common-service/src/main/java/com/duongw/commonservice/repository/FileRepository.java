package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.model.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<Files, Long> {


    Files findByBusinessId(Long businessId);
    List<Files> findByBusinessCode(String businessCode);


    void deleteByBusinessId(Long businessId);
    void deleteByBusinessCode(String businessCode);

}
