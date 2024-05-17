package com.bamboo.repository;

import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {

    List<Board> findByWriter(String writer);

    List<Board> findByWriterOrContent(String writer, String content);

    @Query("select i from Board i where i.content like %:content%")
    List<Board> findByContent(@Param("content") String content);

    @Query(value = "select * from board i where i.board_content like " + "%:content%", nativeQuery = true)
    List<Board> findByContentByNative(@Param("content") String content);
}