package com.duongw.commonservice.model.entity;

import com.duongw.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "SEND_MAIL")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "send_mail_seq_gen")
    @SequenceGenerator(name = "send_mail_seq_gen", sequenceName = "COMMON.SEND_MAIL_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "MAIL_TO")
    private String mailTo;

    @Column(name = "STATUS")
    private Long status;


}
