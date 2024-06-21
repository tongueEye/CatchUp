package com.catchup.catchup.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "info_board")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class InfoBoard extends BoardBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iid;

    @Column(name = "rep_content")
    private String repContent;

    //uid 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    User user;

}
