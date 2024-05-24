package com.bamboo.repository;


import com.bamboo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board>, BoardFileRepositoryCustom {

    List<Board> findByTitle(String title);

    List<Board> findByTitleOrContent(String title, String content);

    @Query("select i from Board i where i.content like %:content%")
    List<Board> findByContent(@Param("content") String content);

    @Query(value = "select * from board i where i.board_content like " + "%:content%", nativeQuery = true)
    List<Board> findByContentByNative(@Param("content") String content);

    @Query("SELECT b FROM Board b WHERE b.isDeleted = false ORDER BY b.regTime DESC")
    Page<Board> findAllByIsDeletedFalse(Pageable pageable);


    // 검색 조건이 있는 경우 추가
    @Query("SELECT b FROM Board b WHERE b.isDeleted = false AND " +
            "(b.title LIKE %:searchQuery% OR b.content LIKE %:searchQuery% OR b.createdBy LIKE %:searchQuery%) " +
            "ORDER BY b.regTime DESC")
    Page<Board> searchBoards(String searchQuery, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.id = :id AND b.isDeleted = false")
    Optional<Board> findById(@Param("id") Long id);


}
