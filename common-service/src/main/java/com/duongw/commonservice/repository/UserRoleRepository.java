package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.model.entity.UserRole;
import com.duongw.commonservice.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByUserId(Long userId);
    List<UserRole> findAllByUserId(Long roleId);
}
