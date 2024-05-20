package com.bamboo.repository;

import com.bamboo.dto.BoardSearchDto;
import com.bamboo.dto.MainDto;
import com.bamboo.dto.QMainDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.QBoard;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private final QBoard board = QBoard.board;

    private BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("boardTitle", searchBy)) {
            return QBoard.board.title.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("createBy", searchBy)){
            return QBoard.board.createdBy.like("%" + searchQuery +"%");
        }

        return null;
    }


    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {

        List<Board> content = queryFactory
                .selectFrom(board)
                .where(searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery()))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(board)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        if(total == null)
            total = 0L;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression boardTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.title.like("%" + searchQuery + "%");
    }
    @Override
    public Page<MainDto> getMainPage(BoardSearchDto boardSearchDto, Pageable pageable) {

        List<MainDto> content = queryFactory
                .select(
                        new QMainDto(
                                board.id,
                                board.title,
                                board.member,
                                board.hit,
                                board.regTime))
                .from(board)
                .where(searchByLike("title", boardSearchDto.getSearchQuery()))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(board)
                .where(searchByLike("title", boardSearchDto.getSearchQuery()))
                .fetchOne();

        if(total == null)
            total = 0L;

        return new PageImpl<>(content, pageable, total);
    }


}
