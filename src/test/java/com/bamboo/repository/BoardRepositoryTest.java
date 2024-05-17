//package com.bamboo.repository;
//
//import com.bamboo.dto.BoardDto;
//import com.bamboo.entity.Board;
//import com.bamboo.entity.Member;
//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
//class BoardRepositoryTest {
//
//    @Autowired
//    BoardRepository boardRepository;
//
//
//    @Test
//    @DisplayName("상품 저장 테스트")
//    public void  createItemTest(){
//
//        Board board = new Board();
//        board.setTitle("테스트");
//        board.setWriter("테스트");
//        board.setContent("테스트설명");
//        board.setGood(1L);
//        board.setHit(10L);
//        board.setRegTime(LocalDateTime.now());
//        board.setUpdateTime(LocalDateTime.now());
//
//        Board savedBoard = boardRepository.save(board);
//        System.out.println(savedBoard);
//    }
//
//    public void createBoardList(){
//
//        for(int i = 1; i <= 10; i++){
//            Board board = new Board();
//
//            board.setTitle("테스트 상품" + i);
//            board.setWriter("테스트" + i);
//            board.setContent("테스트 설명" + i);
//            board.setHit(10L);
//            board.setGood(100L);
//            board.setRegTime(LocalDateTime.now());
//            board.setUpdateTime(LocalDateTime.now());
//
//            Board savedItem = boardRepository.save(board);
//        }
//    }
//
//    @Test
//    @DisplayName("작성자 조회 테스트")
//    public void findByWriterTest(){
//
//        this.createBoardList();
//        List<Board> boardList = boardRepository.findByWriter("테스트 1");
//        for(Board board : boardList){
//            System.out.println(board.toString());
//        }
//    }
//
//    @Test
//    @DisplayName("작성자, 글 설명 or 테스트")
//    public void findByWriterOrContent(){
//
//        this.createBoardList();
//        List<Board> boardList = boardRepository.findByWriterOrContent("테스트 1", "테스트 설명 5");
//        for(Board board : boardList){
//            System.out.println(board.toString());
//        }
//    }
//
//    @Test
//    @DisplayName("@Query를 이용한 상품 조회 테스트")
//    public void findByContentTest(){
//
//        this.createBoardList();
//        List<Board> boardList = boardRepository.findByContent("테스트 설명");
//        for(Board board : boardList){
//            System.out.println(board.toString());
//        }
//    }
//
//    @Test
//    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
//    public void findByContentByNativeTest(){
//
//        this.createBoardList();
//        List<Board> boardList = boardRepository.findByContentByNative("테스트 설명");
//        for(Board board : boardList){
//            System.out.println(board.toString());
//        }
//    }
//
//}