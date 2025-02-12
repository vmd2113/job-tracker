package com.duongw.workforceservice.model.entity;


import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "WO_CONFIG_BUSINESS", schema = "WFM")
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkConfigBusiness extends BaseEntity {

    @Id
    @Column(name = "CONFIG_BUSINESS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_config_business_seq_gen")
    @SequenceGenerator(name = "work_config_business_seq_gen", sequenceName = "WFM.WO_CONFIG_BUSINESS_SEQ", allocationSize = 1)
    private Long workConfigId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WO_TYPE_ID")
    private WorkType workType;

    @Column(name = "PRIORITY_ID")
    private Long priorityId;


    @Column(name = "OLD_STATUS")
    private Long oldStatus;

    @Column(name = "NEW_STATUS")
    private Long newStatus;
}
