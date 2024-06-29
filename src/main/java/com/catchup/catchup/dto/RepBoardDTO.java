package com.catchup.catchup.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepBoardDTO {

  private Long frid;
  private Long fid;
  private Long uid;
  private String frcontent;
  private LocalDateTime frCreateDate;
  private LocalDateTime frUpdateDate;

  private String title;
  private String cate;



}
