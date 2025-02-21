package com.duongw.commonservice.repository;

import com.duongw.commonservice.model.entity.ConfigView;
import com.duongw.commonservice.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMailRepository extends JpaRepository<Item, Long> {
}
