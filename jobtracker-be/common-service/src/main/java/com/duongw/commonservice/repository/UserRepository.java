package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);



}
