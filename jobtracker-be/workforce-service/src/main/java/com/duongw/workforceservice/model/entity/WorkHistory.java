package com.duongw.workforceservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "WO_HISTORY", schema = "WFM")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkHistory extends BaseEntity {

    @Id
    @Column(name = "WO_HISTORY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wo_history_seq_gen")
    @SequenceGenerator(name = "wo_history_seq_gen", sequenceName = "WFM.WO_HISTORY_SEQ", allocationSize = 1)
    private Long workHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WO_ID")
    private Works work;

    @Column(name = "WO_CODE")
    private String workCode;

    @Column(name = "WO_CONTENT")
    private String workContent;
}
