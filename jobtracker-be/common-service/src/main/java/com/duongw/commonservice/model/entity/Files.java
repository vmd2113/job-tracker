package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "FILES")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Files extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_seq_gen")
    @SequenceGenerator(name = "files_seq_gen", sequenceName = "COMMON.FILES_SEQ", allocationSize = 1)
    @Column(name = "FILE_ID")
    private Long fieldId;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "BUSINESS_CODE")
    private String businessCode;

    @Column(name = "BUSINESS_ID")
    private Long businessId;

    @Column(name = "STATUS")
    private Long status;


}


