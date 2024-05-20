package com.bamboo.dto;

import com.bamboo.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MainDto {

    private Long id;
    private String title;
    private Member name;
    private Long hit;
    private LocalDateTime regTime;

    @QueryProjection
    public MainDto(Long id, String title, Member name, Long hit, LocalDateTime regTime){
        this.id = id;
        this.title = title;
        this.name = name;
        this.hit = hit;
        this.regTime = regTime;
    }
}
