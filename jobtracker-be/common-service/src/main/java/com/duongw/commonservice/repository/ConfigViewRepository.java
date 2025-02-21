package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigViewRepository extends JpaRepository<ConfigView, Long> {
    ConfigView findByViewName(String name);

    ConfigView findByViewPath(String path);

    ConfigView findByApiPath(String apiPath);

    List<ConfigView> findByRoleId(String roleId);

    List<ConfigView> findByStatus(Long status);
}
