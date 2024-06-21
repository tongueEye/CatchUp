package com.catchup.catchup.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeBoardDTO {

    private Long fid;
    private String title;
    private String cate;
    private String writer;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer cnt;
    private String link;
    private String kind;
    private String profile;

}
