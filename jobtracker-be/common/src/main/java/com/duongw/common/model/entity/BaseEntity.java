package com.duongw.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @Column(name = "CREATED_TIME")
    private LocalDateTime createDate;

    @Column(name = "UPDATED_TIME")
    private LocalDateTime updateDate;

    @Column(name = "CREATED_USER")
    private Long createdByUser;

    @Column(name = "UPDATED_USER")
    private Long updatedByUser;


    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.createdByUser = getCurrentUser();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
        this.updatedByUser = getCurrentUser();
    }


    //TODO: implement get current user
    private long getCurrentUser() {
        return 1L;
    }


}