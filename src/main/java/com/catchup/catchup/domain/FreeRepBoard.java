package com.catchup.catchup.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "rep_board")
@EntityListeners(AuditingEntityListener.class)
public class FreeRepBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long frid;

    private String frcontent;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime frCreateDate;

    @Column(name = "update_date")
    @LastModifiedDate
    private LocalDateTime frUpdateDate;

    //uid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    User user;

    //fid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    FreeBoard freeBoard;
}
