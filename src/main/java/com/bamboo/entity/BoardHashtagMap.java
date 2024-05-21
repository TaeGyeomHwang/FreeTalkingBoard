package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board_hashtag_map")
@Getter
@Setter
@ToString
public class BoardHashtagMap extends BaseEntity{

    @Id
    @Column(name = "board_hashtag_map_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public static BoardHashtagMap createBoardHashtagMap(Hashtag hashtag, Board board) {
        BoardHashtagMap boardHashtagMap = new BoardHashtagMap();

        boardHashtagMap.setHashtag(hashtag);
        boardHashtagMap.setBoard(board);

        return boardHashtagMap;
    }
}
