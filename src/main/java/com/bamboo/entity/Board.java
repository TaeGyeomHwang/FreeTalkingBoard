package com.bamboo.entity;

import com.bamboo.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //글 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //유저

    @Column(nullable = false, name = "board_title")
    private String title; //제목

    @Lob
    @Column(name = "board_content", nullable = false)
    private String content; //내용

    @Column(name = "board_is_deleted")
    private boolean isDeleted; //삭제 여부

    private Long good; //좋아요

    private Long hit; //조회수

    private LocalDateTime regTime; //등록시간

    private LocalDateTime updateTime; //수정 시간

    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
    }


}
