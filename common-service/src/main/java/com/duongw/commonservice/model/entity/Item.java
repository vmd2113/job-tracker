package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity

@Table(name = "ITEM")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "COMMON.ITEM_SEQ", allocationSize = 1)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ITEM_VALUE")
    private String itemValue;

    @Column(name = "PARENT_ITEM_ID")
    private Long parentItemId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "STATUS")
    private Long status;





}
