package com.bamboo.service;

import com.bamboo.dto.BoardFileDto;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.dto.BoardSearchDto;
import com.bamboo.dto.HashtagDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.BoardHashtagMap;
import com.bamboo.entity.Hashtag;
import com.bamboo.repository.BoardFileRepository;
import com.bamboo.repository.BoardHashtagMapRepository;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.HashTagRepository;
import com.bamboo.service.BoardFileService;
import com.bamboo.service.HashTagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileService boardFileService;
    private final BoardFileRepository boardFileRepository;
    private final HashTagService hashTagService;
    private final BoardHashtagMapRepository boardHashtagMapRepository;
    private final HashTagRepository hashtagRepository;

    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFiles) throws Exception {
        Board board = boardFormDto.createBoard();
        board.setDeleted(false);
        boardRepository.save(board);

        // 파일 저장
        if (boardFiles != null && !boardFiles.isEmpty()) {
            for (MultipartFile multipartFile : boardFiles) {
                if (!multipartFile.isEmpty()) {
                    BoardFile boardFile = new BoardFile();
                    boardFile.setBoard(board);
                    boardFileService.saveBoardFile(boardFile, multipartFile); // BoardFileService를 사용하여 파일 저장
                }
            }
        }

        // 해시태그 저장 및 매핑
        if (boardFormDto.getHashtag() != null && !boardFormDto.getHashtag().isEmpty()) {
            String[] hashtags = boardFormDto.getHashtag().split(" ");
            for (String tagName : hashtags) {
                String trimmedTagName = tagName.trim();
                if (!trimmedTagName.isEmpty()) {
                    Hashtag hashtag = hashtagRepository.findByName(trimmedTagName).orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setName(trimmedTagName);
                        return newHashtag;
                    });
                    hashtagRepository.save(hashtag);

                    BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
                    boardHashtagMap.setBoard(board);
                    boardHashtagMap.setHashtag(hashtag);
                    boardHashtagMapRepository.save(boardHashtagMap);
                }
            }
        }

        return board.getId();
    }

    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {
        List<BoardFile> boardFileList = boardFileRepository.findByBoardId(boardId);
        List<BoardFileDto> boardFileDtoList = new ArrayList<>();
        for (BoardFile boardFile : boardFileList) {
            BoardFileDto boardFileDto = BoardFileDto.of(boardFile);
            boardFileDtoList.add(boardFileDto);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        boardFormDto.setBoardFileDtoList(boardFileDtoList);

        // 해시태그 조회 및 설정
        List<BoardHashtagMap> boardHashtagMaps = boardHashtagMapRepository.findByBoard(board);
        List<HashtagDto> hashtagDtoList = new ArrayList<>();
        List<String> hashtags = new ArrayList<>();
        for (BoardHashtagMap boardHashtagMap : boardHashtagMaps) {
            HashtagDto hashtagDto = new HashtagDto();
            hashtagDto.setTagName(boardHashtagMap.getHashtag().getName());
            hashtagDtoList.add(hashtagDto);
            hashtags.add(boardHashtagMap.getHashtag().getName());
        }
        boardFormDto.setHashtagDtoList(hashtagDtoList);
        boardFormDto.setHashtag(String.join(" ", hashtags)); // 해시태그 문자열 설정

        // 디버그 로그 추가
        System.out.println("Hashtags: " + String.join(", ", hashtags));

        return boardFormDto;
    }

    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);

        List<Long> boardFileIds = boardFormDto.getBoardFileIds();
        List<BoardFile> existingFiles = boardFileRepository.findByBoardId(board.getId());

        // 기존 파일 중 삭제할 파일 처리
        for (BoardFile existingFile : existingFiles) {
            if (boardFileIds == null || !boardFileIds.contains(existingFile.getId())) {
                boardFileService.deleteBoardFile(existingFile);
            }
        }

        // 새 파일 추가
        if (boardFileList != null && !boardFileList.isEmpty()) {
            for (MultipartFile multipartFile : boardFileList) {
                if (!multipartFile.isEmpty() && (boardFileIds == null || !boardFileIds.contains(multipartFile.hashCode()))) {
                    BoardFile boardFile = new BoardFile();
                    boardFile.setBoard(board);
                    boardFileService.saveBoardFile(boardFile, multipartFile);
                }
            }
        }

        // 기존 해시태그 매핑 삭제
        boardHashtagMapRepository.deleteByBoard(board);

        // 새로운 해시태그 저장 및 매핑
        if (boardFormDto.getHashtag() != null && !boardFormDto.getHashtag().isEmpty()) {
            String[] hashtags = boardFormDto.getHashtag().split(" ");
            for (String tagName : hashtags) {
                String trimmedTagName = tagName.trim();
                if (!trimmedTagName.isEmpty()) {
                    Hashtag hashtag = hashtagRepository.findByName(trimmedTagName).orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setName(trimmedTagName);
                        return newHashtag;
                    });
                    hashtagRepository.save(hashtag);

                    BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
                    boardHashtagMap.setBoard(board);
                    boardHashtagMap.setHashtag(hashtag);
                    boardHashtagMapRepository.save(boardHashtagMap);
                }
            }
        }

        return board.getId();
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        if (boardSearchDto.getSearchQuery() == null || boardSearchDto.getSearchQuery().isEmpty()) {
            return boardRepository.findAllByIsDeletedFalse(pageable);
        } else {
            return boardRepository.searchBoards(boardSearchDto.getSearchQuery(), pageable);
        }
    }

    public Board getBoardHit(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            Board hitBoard = board.get();
            if (hitBoard.isDeleted()) {
                System.out.println("Board is deleted, id: " + id);
                throw new EntityNotFoundException("Board not found");
            }
            Long currentHit = hitBoard.getHit();
            hitBoard.setHit((currentHit == null ? 0 : currentHit) + 1);
            this.boardRepository.save(hitBoard);
            System.out.println("Hit updated for board id: " + id);
            return hitBoard;
        } else {
            System.out.println("Board not found, id: " + id);
            throw new EntityNotFoundException("Board not found");
        }
    }


    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.setDeleted(true);
        boardRepository.save(board);
    }
}
