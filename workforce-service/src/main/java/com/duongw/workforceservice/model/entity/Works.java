package com.duongw.workforceservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "WO", schema = "WFM")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Works extends BaseEntity {

    @Id
    @Column(name = "WO_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "works_seq_gen")
    @SequenceGenerator(name = "works_seq_gen", sequenceName = "WFM.WO_SEQ", allocationSize = 1)
    private Long worksId;

    @Column(name = "WO_CODE")
    private String workCode;

    @Column(name = "WO_CONTENT")
    private String workContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WO_TYPE_ID")
    private WorkType workType;

    @Column(name = "PRIORITY_ID")
    private Long priorityId;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "FINISH_TIME")
    private LocalDateTime finishTime;

    @Column(name = "ASSIGN_USER_ID")
    private Long assignedUserId;

    @OneToMany(mappedBy = "work", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WorkHistory> workHistoryList;
}
