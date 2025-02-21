package com.duongw.workforceservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "WO_TYPE", schema = "WFM")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class WorkType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_type_seq_gen")
    @SequenceGenerator(name = "work_type_seq_gen", sequenceName = "WFM.WO_TYPE_SEQ", allocationSize = 1)
    @Column(name = "WO_TYPE_ID")
    private Long workTypeId;

    @Column(name = "WO_TYPE_CODE", unique = true)
    private String workTypeCode;

    @Column(name = "WO_TYPE_NAME", unique = true)
    private String workTypeName;

    @Column(name = "PRIORITY_ID")
    private Long priorityId;

    @Column(name = "PROCESS_TIME")
    private Float processTime;

    @Column(name = "STATUS")
    private Long status;

}
