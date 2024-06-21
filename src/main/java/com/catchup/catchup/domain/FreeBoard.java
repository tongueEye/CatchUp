package com.catchup.catchup.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "free_board")
@EntityListeners(AuditingEntityListener.class)
public class FreeBoard extends BoardBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    private Integer count;
    private String link;
}
