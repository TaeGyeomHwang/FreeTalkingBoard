package com.bamboo.repository;

import com.bamboo.dto.BoardSearchDto;
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

public class BoardFileRepositoryCustomImpl implements BoardFileRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;

    public BoardFileRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return board.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return board.content.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return board.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        List<Board> content = queryFactory
                .selectFrom(board)
                .leftJoin(board.member)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(Wildcard.count)
                .from(board)
                .where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(content, pageable, total);
    }
}



