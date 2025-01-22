package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "DEPARTMENT")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq_gen")
    @SequenceGenerator(name = "department_seq_gen", sequenceName = "COMMON.DEPARTMENT_SEQ", allocationSize = 1)
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "DEPARTMENT_CODE")
    private String departmentCode;

    @Column(name = "STATUS")
    private Long status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_DEPARTMENT_ID")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment")
    private List<Department> subDepartment;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Users> users;




}
