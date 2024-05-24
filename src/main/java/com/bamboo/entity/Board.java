package com.bamboo.entity;

import com.bamboo.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity{

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
    private boolean isDeleted = false; //삭제 여부

    private Long good; //좋아요

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long hit = 0L; // 조회수, 기본값 0

    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
    }


}
