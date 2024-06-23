package com.catchup.catchup.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoBoardDTO {
    private Long iid;
    private String title;
    private String content;
    private String cate;
    private String writer;
    private LocalDateTime createDate;
    private String repContent;
    private String kind;
    private Long uid;
}
