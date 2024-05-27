package com.bamboo.service;

import com.bamboo.dto.BoardFormDto;
import com.bamboo.entity.Board;
import com.bamboo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void saveBoard(BoardFormDto boardFormDto, List<String> hashtags) {
        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        boardRepository.save(board);
    }

}
