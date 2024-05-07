package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, name = "board_title")
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;
}
