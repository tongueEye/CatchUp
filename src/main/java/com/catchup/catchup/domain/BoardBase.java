package com.catchup.catchup.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BoardBase {
    private String title;
    private String content;
    private String cate;
    private String writer;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;
    private String kind;

    //uid 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    User user;

}
