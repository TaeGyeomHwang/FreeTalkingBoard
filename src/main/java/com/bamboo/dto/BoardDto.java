package com.bamboo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDto {

    private Long id;
    private String writer;
    private String title; //제목
    private String content; //내용
    private Long good; //좋아요 수
    private Long hit; //조회 수

    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
