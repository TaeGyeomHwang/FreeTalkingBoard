package com.bamboo.service;

import com.bamboo.dto.BoardDto;
import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.Member;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;

    private final BoardFileService boardFileService;

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardRepository.getBoardPage(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Board> getSortedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable, String sort) {
        return boardRepository.getSortedBoardPage(boardSearchDto, pageable, sort);
    }

    public Long saveBoard(String email, BoardDto boardDto, List<MultipartFile> boardFileList) throws Exception {
        Board board = boardDto.createBoard();
        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        board.setMember(member);
        boardRepository.save(board);

        for(int i=0;i<boardFileList.size();i++){
            BoardFile boardFile = new BoardFile();
            boardFile.setBoard(board);

            boardFileService.saveBoardFile(boardFile, boardFileList.get(i));
        }

        return board.getId();
    }
}
