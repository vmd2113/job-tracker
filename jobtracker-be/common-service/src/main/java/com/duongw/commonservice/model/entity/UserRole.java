package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "USER_ROLES")
@Getter
@Setter
public class UserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_seq_gen")
    @SequenceGenerator(name = "user_roles_seq_gen", sequenceName = "COMMON.USER_ROLES_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "ROLE_ID")
    private Long roleId;




}
