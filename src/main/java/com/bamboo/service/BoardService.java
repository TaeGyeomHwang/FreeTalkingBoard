package com.bamboo.service;

import com.bamboo.dto.BoardFileDto;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.dto.HashtagDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.BoardHashtagMap;
import com.bamboo.entity.Hashtag;
import com.bamboo.repository.BoardFileRepository;
import com.bamboo.repository.BoardHashtagMapRepository;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.HashTagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final BoardFileService boardFileService;
    private final BoardFileRepository boardFileRepository;
    private final HashTagService hashTagService;
    private final BoardHashtagMapRepository boardHashtagMapRepository;
    private final HashTagRepository hashtagRepository;

    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {
        Board board = boardFormDto.createBoard();
        boardRepository.save(board);
        System.out.println("Board saved successfully");

        // 파일 저장
        for (int i = 0; i < boardFileList.size(); i++) {
            BoardFile boardFile = new BoardFile();
            boardFile.setBoard(board);
            try {
                boardFileService.saveBoardFile(boardFile, boardFileList.get(i));
                System.out.println("BoardFile saved successfully: " + boardFileList.get(i).getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error saving BoardFile: " + boardFileList.get(i).getOriginalFilename());
                throw e;
            }
        }

        // 해시태그 저장 및 매핑
        String[] hashtags = boardFormDto.getHashtag().split(",");
        for (String tagName : hashtags) {
            String trimmedTagName = tagName.trim();
            Hashtag hashtag = hashtagRepository.findByName(trimmedTagName).orElseGet(() -> {
                Hashtag newHashtag = new Hashtag();
                newHashtag.setName(trimmedTagName);
                return newHashtag;
            });
            hashtagRepository.save(hashtag);
            System.out.println("Hashtag saved successfully: " + trimmedTagName);

            BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
            boardHashtagMap.setBoard(board);
            boardHashtagMap.setHashtag(hashtag);
            boardHashtagMapRepository.save(boardHashtagMap);
            System.out.println("BoardHashtagMap saved successfully");
        }

        return board.getId();
    }


    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {

        List<BoardFile> boardFileList = boardFileRepository.findByIdOrderByIdAsc(boardId);
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
        boardFormDto.setHashtag(String.join(", ", hashtags)); // 해시태그 문자열 설정

        // 디버그 로그 추가
        System.out.println("Hashtags: " + String.join(", ", hashtags));

        return boardFormDto;
    }

    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {

        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);

        List<Long> boardFileIds = boardFormDto.getBoardFileIds();

        for (int i = 0; i < boardFileList.size(); i++) {
            boardFileService.updateBoardFile(boardFileIds.get(i), boardFileList.get(i));
        }

        // 해시태그 업데이트
        boardHashtagMapRepository.deleteByBoard(board);
        for (HashtagDto hashtagDto : boardFormDto.getHashtagDtoList()) {
            Hashtag hashtag = hashtagRepository.findByName(hashtagDto.getTagName())
                    .orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setName(hashtagDto.getTagName());
                        return newHashtag;
                    });
            hashtagRepository.save(hashtag);

            BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
            boardHashtagMap.setBoard(board);
            boardHashtagMap.setHashtag(hashtag);
            boardHashtagMapRepository.save(boardHashtagMap);
        }

        return board.getId();
    }
}
