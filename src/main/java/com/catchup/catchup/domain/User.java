package com.catchup.catchup.domain;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter @Setter
@Table(name = "user")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String id;
    private String password;
    private String nickname;
    private String name;
    private String addr;
    private String profile;
    private String phone;

    @Column(name = "ATPT_OFCDC_SC_CODE")
    private String sidoCode;

    @Column(name = "SD_SCHUL_CODE")
    private String sDCode;

    @Column(name = "SCHUL_NM")
    private String schoolName;
  
    @OneToMany(mappedBy = "user")
    private List<FreeBoard> boardList = new ArrayList<>();


}
