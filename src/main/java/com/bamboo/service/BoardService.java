package com.bamboo.service;

import com.bamboo.dto.BoardFileDto;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.repository.BoardFileRepository;
import com.bamboo.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardFileService boardFileService;

    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> multipartFiles) throws Exception {

        Board board = boardFormDto.createBoard();
        boardRepository.save(board);

        for(int i=0; i < multipartFiles.size(); i++) {
            BoardFile boardFile = new BoardFile();
            boardFile.setBoard(board);

            boardFileService.saveBoardFile(boardFile, multipartFiles.get(i));
        }

        return board.getId();
    }

    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {

        List<BoardFile> boardFileList = boardFileRepository.findByBoardIdOrderByIdAsc(boardId);
        List<BoardFileDto> boardFileDtoList = new ArrayList<>();

        for(BoardFile boardFile : boardFileList) {
            BoardFileDto boardFileDto = BoardFileDto.of(boardFile);
            boardFileDtoList.add(boardFileDto);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        boardFormDto.setBoardFileDtoList(boardFileDtoList);

        return boardFormDto;

    }

    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> multipartFiles) throws Exception{
        //상품 수정
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);
        List<Long> boardFileIds = boardFormDto.getBoardFormIds();

        //이미지 등록
        for(int i=0;i<multipartFiles.size();i++){
            boardFileService.updateBoardFile(boardFileIds.get(i),
                    multipartFiles.get(i));
        }

        return board.getId();
    }


}
