package com.bamboo.service;

import com.bamboo.dto.BoardDto;
import com.bamboo.dto.BoardFileDto;
import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.BoardMemberMap;
import com.bamboo.entity.Member;
import com.bamboo.repository.BoardFileRepository;
import com.bamboo.repository.BoardMemberMapRepository;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;

    private final BoardFileRepository boardFileRepository;

    private final BoardMemberMapRepository boardMemberMapRepository;

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

    @Transactional(readOnly = true)
    public BoardDto getBoardDtl(Long boardId){
        List<BoardFile> boardFileList = boardFileRepository.findAllByBoardIdOrderByIdAsc(boardId);
        List<BoardFileDto> boardFileDtoList = new ArrayList<>();
        for (BoardFile boardFile : boardFileList){
            BoardFileDto boardFileDto = BoardFileDto.of(boardFile);
            boardFileDtoList.add(boardFileDto);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardDto boardDto = BoardDto.of(board);
        boardDto.setBoardFileDtoList(boardFileDtoList);

        Member member = board.getMember();
        boardDto.setMember(member);

        return boardDto;
    }

    public void setHit(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        board.setHit(board.getHit()+1);
        boardRepository.save(board);
    }

    public void setGood(Long boardId, String email) {
        // 이미 좋아요 눌렀는지 확인
        BoardMemberMap existingLike = boardMemberMapRepository.findByBoardIdAndMemberEmail(boardId, email)
                .orElseThrow(EntityNotFoundException::new);
        if (existingLike != null) {
            throw new IllegalStateException("좋아요는 한 글에 한 번만 가능합니다");
        }

        // 매핑 테이블 생성
        BoardMemberMap like = new BoardMemberMap();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        like.setBoard(board);
        like.setMember(member);
        boardMemberMapRepository.save(like);

        // 좋아요 수 증가
        board.setGood(board.getGood() + 1);
        boardRepository.save(board);
    }
}
