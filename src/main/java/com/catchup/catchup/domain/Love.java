package com.catchup.catchup.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lid;

    //uid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    //fid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private FreeBoard freeBoard;
}
