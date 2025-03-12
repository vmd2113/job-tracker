package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigViewRepository extends JpaRepository<ConfigView, Long> {


    ConfigView findByViewPath(String path);

    ConfigView findByApiPath(String apiPath);

    List<ConfigView> findByStatus(Long status);

    ConfigView findByViewName(String name);
}
