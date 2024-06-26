package com.catchup.catchup.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BoardBase {
  
    private String title;
    private String content;
    private String cate;
    private String writer;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    private String kind;

}
