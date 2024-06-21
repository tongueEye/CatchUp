package com.catchup.catchup.domain;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String id;
    private String password;
    private String nickname;
    private String addr;
    private String profile;
    private String phone;


    @Column(name = "school_code")
    private String schoolCode;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "local_code")
    private String localCode;
}