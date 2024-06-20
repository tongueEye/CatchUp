package com.catchup.catchup.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeBoardDTO {

    private Long fid;
    private LocalDateTime updateDate;
    private Integer count;
    private String link;
}
