package com.bamboo.entity;

import com.bamboo.dto.BoardFileDto;
import com.bamboo.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Board extends BaseTimeEntity{

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member; //작성자

    private String writer;

    @Column(nullable = false, name = "board_title")
    private String title; //제목

    @Lob
    @Column(name = "board_content", nullable = false)
    private String content; //내용

    @Column(name = "board_is_deleted")
    private boolean isDeleted;

    @Column(name = "board_is_restored")
    private boolean isRestored;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private Long good; //좋아요 수

    private Long hit; //조회 수

    public void updateBoard(BoardFormDto boardFormDto){
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
    }
    
}
