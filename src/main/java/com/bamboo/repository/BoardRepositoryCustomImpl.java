package com.bamboo.repository;

import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.QBoard;
import com.bamboo.entity.QBoardHashtagMap;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;
    private final QBoardHashtagMap boardHashtagMap = QBoardHashtagMap.boardHashtagMap;


    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (searchQuery.startsWith("#")) {
            String[] hashtagNames = searchQuery.split("#");
            BooleanExpression expression = null;
            for (String hashtagName : hashtagNames) {
                if (!hashtagName.isEmpty()) {
                    BooleanExpression hashtagExpression = boardHashtagMap.hashtag.name.eq(hashtagName.trim());
                    expression = (expression == null) ? hashtagExpression : expression.or(hashtagExpression);
                }
            }
            return expression;
        } else if (StringUtils.equals("title", searchBy)) {
            return board.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return board.content.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("name", searchBy)) {
            return board.member.name.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        BooleanExpression searchExpression = searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery());

        // 조건에 따라 검색한 글 목록 쿼리
        JPAQuery<Board> contentQuery = queryFactory
                .selectFrom(board)
                .leftJoin(boardHashtagMap).on(board.id.eq(boardHashtagMap.board.id))
                .where(searchExpression)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 전체 count 수 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(board.id.count())
                .from(board)
                .leftJoin(boardHashtagMap).on(board.id.eq(boardHashtagMap.board.id))
                .where(searchExpression);

        List<Board> content = contentQuery.fetch();
        long total = countQuery.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<Board> getSortedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable, String sortBy) {
        // Define your query expressions
        BooleanExpression searchExpression = searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery());
        OrderSpecifier<?> orderSpecifier;

        // Determine the sorting attribute and create an OrderSpecifier accordingly
        switch (sortBy) {
            case "title":
                orderSpecifier = board.title.asc();
                break;
            case "member.name":
                orderSpecifier = board.member.name.asc();
                break;
            case "hit":
                orderSpecifier = board.hit.desc(); // Example of descending order
                break;
            case "regTime":
                orderSpecifier = board.regTime.asc();
                break;
            default:
                // Default sorting if no valid attribute is provided
                orderSpecifier = board.id.desc(); // For example, sorting by ID in descending order
                break;
        }

        // Execute the query with sorting and pagination
        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .leftJoin(boardHashtagMap).on(board.id.eq(boardHashtagMap.board.id))
                .where(searchExpression)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // Fetch the content and total count
        List<Board> content = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
