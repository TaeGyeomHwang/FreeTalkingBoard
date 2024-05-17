//package com.bamboo.entity;
//
//import com.bamboo.repository.BoardRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//class BoardTest {
//
//    @Test
//    void save() {
//
//        // 1. 게시글 파라미터 생성
//        Board params = Board.builder()
//                .title("1번 게시글 제목")
//                .content("1번 게시글 내용")
//                .writer("도뎡이")
//                .good(0L)
//                .hit(0L)
//                .build();
//    }
//}