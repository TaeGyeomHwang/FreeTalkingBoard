//package com.bamboo.service;
//
//import com.bamboo.dto.BoardFormDto;
//import com.bamboo.entity.Board;
//import com.bamboo.entity.BoardFile;
//import com.bamboo.repository.BoardFileRepository;
//import com.bamboo.repository.BoardRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//class BoardServiceTest {
//
//    @Autowired
//    BoardService boardService;
//
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Autowired
//    BoardFileRepository boardFileRepository;
//
//    List<MultipartFile> createMultipartFiles() throws Exception {
//
//        List<MultipartFile> mulitipartFileList = new ArrayList<>();
//
//        for(int i=0; i<5; i++) {
//            String path = "C:/board/item/";
//            String imageName = "image" + i + ".jpg";
//
//            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
//            mulitipartFileList.add(multipartFile);
//
//        }
//
//        return mulitipartFileList;
//    }
//
//    @Test
//    @DisplayName("글 등록 테스트")
//    void saveItem() throws Exception {
//
//        BoardFormDto boardFormDto = new BoardFormDto();
//        boardFormDto.setTitle("테스트 제목");
//        boardFormDto.setContent("테스트 내용");
//
//        List<MultipartFile> multipartFileList = createMultipartFiles();
//        Long itemId = boardService.saveBoard(boardFormDto, multipartFileList);
//
//        List<BoardFile> boardFileList = boardFileRepository.findByBoardIdOrderByIdAsc(itemId);
//        Board board = boardRepository.findById(itemId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        assertEquals(boardFormDto.getTitle(), board.getTitle());
//        assertEquals(boardFormDto.getContent(), board.getContent());
//        assertEquals(multipartFileList.get(0).getOriginalFilename(), boardFileList.get(0).getOriFileName());
//    }
//
//}