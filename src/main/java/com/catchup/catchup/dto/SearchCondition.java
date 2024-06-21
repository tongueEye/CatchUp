package com.catchup.catchup.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SearchCondition {
    private String cate;
    private String title;
    private String content;
    private String kind;
}
