package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter
@Setter
@ToString
public class Reply extends BaseEntity{

    @Id
    @Column(name = "reply_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reply_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "reply_isdeleted")
    private boolean isDeleted;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

}
