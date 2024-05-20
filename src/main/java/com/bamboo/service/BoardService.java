//package com.bamboo.service;
//
//import com.bamboo.dto.BoardFileDto;
//import com.bamboo.dto.BoardFormDto;
//import com.bamboo.dto.HashtagDto;
//import com.bamboo.entity.Board;
//import com.bamboo.entity.BoardFile;
//import com.bamboo.entity.BoardHashtagMap;
//import com.bamboo.entity.Hashtag;
//import com.bamboo.repository.BoardFileRepository;
//import com.bamboo.repository.BoardRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class BoardService {
//
//    private final BoardRepository boardRepository;
//    private final BoardFileService boardFileService;
//    private final BoardFileRepository boardFileRepository;
//    private final HashtagService hashtagService;
//
//    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {
//
//        Board board = boardFormDto.createBoard();
//        boardRepository.save(board);
//
//        for (int i = 0; i < boardFileList.size(); i++) {
//            BoardFile boardFile = new BoardFile();
//            boardFile.setBoard(board);
//
//            boardFileService.saveBoardFile(boardFile, boardFileList.get(i));
//        }
//
//        for (HashtagDto tagName : boardFormDto.getHashtags()) {
//            HashtagDto hashtagDto = new HashtagDto();
//            hashtagDto.setHashtagName(String.valueOf(tagName));
//            Hashtag hashtag = hashtagService.saveHashtag(hashtagDto);
//            if (hashtag != null) {
//                BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
//                boardHashtagMap.setBoard(board);
//                boardHashtagMap.setHashtag(hashtag);
//                // You need to save the boardHashtagMap here
//            }
//        }
//
//        return board.getId();
//    }
//
//    @Transactional(readOnly = true)
//    public BoardFormDto getBoardDtl(Long boardId){
//
//        List<BoardFile> boardFileList = boardFileRepository.findByIdOrderByIdAsc(boardId);
//        List<BoardFileDto> boardFileDtoList = new ArrayList<>();
//        for(BoardFile boardFile : boardFileList) {
//            BoardFileDto boardFileDto = BoardFileDto.of(boardFile);
//            boardFileDtoList.add(boardFileDto);
//        }
//
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(EntityNotFoundException::new);
//        BoardFormDto boardFormDto = BoardFormDto.of(board);
//        boardFormDto.setBoardFileDtoList(boardFileDtoList);
//
//        return boardFormDto;
//    }
//
//    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {
//
//        Board board = boardRepository.findById(boardFormDto.getId())
//                .orElseThrow(EntityNotFoundException::new);
//        board.updateBoard(boardFormDto);
//
//        List<Long> boardFileIds = boardFormDto.getBoardFileIds();
//
//        for(int i=0; i < boardFileList.size(); i++) {
//            boardFileService.updateBoardFile(boardFileIds.get(i), boardFileList.get(i));
//        }
//
//        return board.getId();
//    }
//
//}
package com.bamboo.service;

import com.bamboo.dto.*;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.BoardHashtagMap;
import com.bamboo.entity.Hashtag;
import com.bamboo.repository.BoardFileRepository;
import com.bamboo.repository.BoardHashtagMapRepository;
import com.bamboo.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileService boardFileService;
    private final BoardFileRepository boardFileRepository;
    private final HashtagService hashtagService;
    private final BoardHashtagMapRepository boardHashtagMapRepository;

    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {

        Board board = boardFormDto.createBoard();
        boardRepository.save(board);

        // Save board files
        for (MultipartFile file : boardFileList) {
            BoardFile boardFile = new BoardFile();
            boardFile.setBoard(board);
            boardFileService.saveBoardFile(boardFile, file);
        }

        // Save hashtags
        for (HashtagDto hashtagDto : boardFormDto.getHashtags()) {
            Hashtag hashtag = hashtagService.saveHashtag(hashtagDto);
            if (hashtag != null) {
                BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
                boardHashtagMap.setBoard(board);
                boardHashtagMap.setHashtag(hashtag);
                boardHashtagMapRepository.save(boardHashtagMap);
            }
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

        // Fetch and set hashtags
        List<BoardHashtagMap> boardHashtagMaps = boardHashtagMapRepository.findByBoard(board);
        List<HashtagDto> hashtagDtoList = boardHashtagMaps.stream()
                .map(boardHashtagMap -> {
                    HashtagDto hashtagDto = new HashtagDto();
                    hashtagDto.setHashtagId(boardHashtagMap.getHashtag().getId());
                    hashtagDto.setHashtagName(boardHashtagMap.getHashtag().getName());
                    return hashtagDto;
                })
                .collect(Collectors.toList());
        boardFormDto.setHashtags(hashtagDtoList);

        return boardFormDto;
    }

    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {

        // Fetch existing board
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);

        // Update board files
        List<Long> boardFileIds = boardFormDto.getBoardFileIds();

        if (boardFileList.size() != boardFileIds.size()) {
            throw new IllegalArgumentException("Mismatch between file IDs and files list size");
        }

        for (int i = 0; i < boardFileList.size(); i++) {
            boardFileService.updateBoardFile(boardFileIds.get(i), boardFileList.get(i));
        }

        // Update hashtags
        List<BoardHashtagMap> existingMaps = boardHashtagMapRepository.findByBoard(board);
        Set<String> existingHashtags = existingMaps.stream()
                .map(map -> map.getHashtag().getName())
                .collect(Collectors.toSet());

        Set<String> newHashtags = boardFormDto.getHashtags().stream()
                .map(HashtagDto::getHashtagName)
                .collect(Collectors.toSet());

        // Remove old hashtags
        for (BoardHashtagMap map : existingMaps) {
            if (!newHashtags.contains(map.getHashtag().getName())) {
                boardHashtagMapRepository.delete(map);
            }
        }

        // Add new hashtags
        for (HashtagDto hashtagDto : boardFormDto.getHashtags()) {
            if (!existingHashtags.contains(hashtagDto.getHashtagName())) {
                Hashtag hashtag = hashtagService.saveHashtag(hashtagDto);
                if (hashtag != null) {
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
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainDto> getMainPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getMainPage(boardSearchDto, pageable);
    }
}



