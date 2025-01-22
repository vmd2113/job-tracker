package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "CATEGORY", schema = "COMMON")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Category extends BaseEntity {
    @Id
    @Column(name = "CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_gen")
    @SequenceGenerator(name = "category_seq_gen", sequenceName = "COMMON.CATEGORY_SEQ", allocationSize = 1)
    private Long categoryId;

    @Column(name = "CATEGORY_CODE", unique = true)
    private String categoryCode;

    @Column(name = "CATEGORY_NAME", unique = true)
    private String categoryName;

    @Column(name = "STATUS")
    private Long status;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Item> items;




}
