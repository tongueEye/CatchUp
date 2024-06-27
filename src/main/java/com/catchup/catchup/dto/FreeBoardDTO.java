package com.catchup.catchup.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeBoardDTO {

    private Long fid;
    private Long uid;
    private String title;
    private String cate;
    private String writer;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer count;
    private String link;
    private String kind;

    private String profile;
    private String nickname;

    private Long frid;
    private String frcontent;
    private LocalDateTime frCreateDate;
    private LocalDateTime frUpdateDate;

}
