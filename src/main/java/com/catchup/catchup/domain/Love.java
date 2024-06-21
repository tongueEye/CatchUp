package com.catchup.catchup.domain;

import jakarta.persistence.*;

@Entity
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lid;

    //uid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    User user;

    //fid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    FreeBoard freeBoard;
}
