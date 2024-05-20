package com.bamboo.service;

import com.bamboo.dto.BoardFormDto;
import com.bamboo.dto.HashtagDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.BoardHashtagMap;
import com.bamboo.entity.Hashtag;
import com.bamboo.repository.BoardFileRepository;
import com.bamboo.repository.BoardHashtagMapRepository;
import com.bamboo.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardFileRepository boardFileRepository;

    @Autowired
    HashtagService hashtagService;

    @Autowired
    BoardHashtagMapRepository boardHashtagMapRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> mulitipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/board/item/";
            String imageName = "file" + i + ".jpg";

            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "file/jpg", new byte[]{1, 2, 3, 4});
            mulitipartFileList.add(multipartFile);
        }

        return mulitipartFileList;
    }

    @Test
    @DisplayName("글 등록 테스트")
//    @WithMockUser(username = "user", roles = "user")
    void saveItem() throws Exception {

        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setTitle("테스트 제목");
        boardFormDto.setContent("테스트 내용");

        List<MultipartFile> multipartFileList = createMultipartFiles();

        // Add hashtags
        List<HashtagDto> hashtags = new ArrayList<>();
        HashtagDto hashtag1 = new HashtagDto();
        hashtag1.setHashtagName("테스트태그1");
        HashtagDto hashtag2 = new HashtagDto();
        hashtag2.setHashtagName("테스트태그2");
        hashtags.add(hashtag1);
        hashtags.add(hashtag2);
        boardFormDto.setHashtags(hashtags);

        Long boardId = boardService.saveBoard(boardFormDto, multipartFileList);

        List<BoardFile> boardFileList = boardFileRepository.findByIdOrderByIdAsc(boardId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(boardFormDto.getTitle(), board.getTitle());
        assertEquals(boardFormDto.getContent(), board.getContent());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), boardFileList.get(0).getOriFileName());

        // Verify hashtags
        List<Hashtag> savedHashtags = boardHashtagMapRepository.findByBoard(board)
                .stream()
                .map(BoardHashtagMap::getHashtag)
                .toList();
        assertEquals(2, savedHashtags.size());
        assertEquals("테스트태그1", savedHashtags.get(0).getName());
        assertEquals("테스트태그2", savedHashtags.get(1).getName());
    }

    @Test
    @DisplayName("글 수정 테스트")
//    @WithMockUser(username = "user", roles = "user")
    void updateBoard() throws Exception {
        // Create initial board
        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setTitle("초기 제목");
        boardFormDto.setContent("초기 내용");

        List<MultipartFile> multipartFileList = createMultipartFiles();

        List<HashtagDto> initialHashtags = new ArrayList<>();
        HashtagDto hashtag1 = new HashtagDto();
        hashtag1.setHashtagName("초기태그1");
        HashtagDto hashtag2 = new HashtagDto();
        hashtag2.setHashtagName("초기태그2");
        initialHashtags.add(hashtag1);
        initialHashtags.add(hashtag2);
        boardFormDto.setHashtags(initialHashtags);

        Long boardId = boardService.saveBoard(boardFormDto, multipartFileList);

        // Fetch saved files and set their IDs in the updated form
        List<BoardFile> savedFiles = boardFileRepository.findByBoardId(boardId); // 사용 가능한 맞춤형 쿼리로 교체
        List<Long> savedFileIds = savedFiles.stream().map(BoardFile::getId).collect(Collectors.toList());

        // Update board
        BoardFormDto updatedBoardFormDto = new BoardFormDto();
        updatedBoardFormDto.setId(boardId);
        updatedBoardFormDto.setTitle("수정된 제목");
        updatedBoardFormDto.setContent("수정된 내용");
        updatedBoardFormDto.setBoardFileIds(savedFileIds);

        List<HashtagDto> updatedHashtags = new ArrayList<>();
        HashtagDto updatedHashtag1 = new HashtagDto();
        updatedHashtag1.setHashtagName("수정된태그1");
        HashtagDto updatedHashtag2 = new HashtagDto();
        updatedHashtag2.setHashtagName("수정된태그2");
        updatedHashtags.add(updatedHashtag1);
        updatedHashtags.add(updatedHashtag2);
        updatedBoardFormDto.setHashtags(updatedHashtags);

        boardService.updateBoard(updatedBoardFormDto, multipartFileList);

        List<BoardFile> boardFileList = boardFileRepository.findByBoardId(boardId); // 사용 가능한 맞춤형 쿼리로 교체
        Board updatedBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(updatedBoardFormDto.getTitle(), updatedBoard.getTitle());
        assertEquals(updatedBoardFormDto.getContent(), updatedBoard.getContent());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), boardFileList.get(0).getOriFileName());

        // Verify updated hashtags
        List<Hashtag> savedHashtags = boardHashtagMapRepository.findByBoard(updatedBoard)
                .stream()
                .map(BoardHashtagMap::getHashtag)
                .toList();
        assertEquals(2, savedHashtags.size());
        assertEquals("수정된태그1", savedHashtags.get(0).getName());
        assertEquals("수정된태그2", savedHashtags.get(1).getName());
    }

}



