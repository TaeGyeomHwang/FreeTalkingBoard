//package com.bamboo.repository;
//
//import com.bamboo.dto.BoardSearchDto;
//import com.bamboo.entity.Board;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
//
//    private JPAQueryFactory queryFactory;
//
//    public BoardRepositoryCustomImpl(EntityManager em){
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    @Override
//    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
//        return null;
//    }
//}
