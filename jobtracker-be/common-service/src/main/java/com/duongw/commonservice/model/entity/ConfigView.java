package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CONFIG_VIEW")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigView extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_view_seq_gen")
    @SequenceGenerator(name = "config_view_seq_gen", sequenceName = "COMMON.CONFIG_VIEW_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VIEW_NAME")
    private String viewName;

    @Column(name = "VIEW_PATH")
    private String viewPath;

    @Column(name = "API_PATH")
    private String apiPath;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "STATUS")
    private Long status;


}


