package com.bamboo.repository;

import com.bamboo.entity.BoardMemberMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardMemberMapRepository extends JpaRepository<BoardMemberMap, Long> {
    @Query("SELECT bmm FROM BoardMemberMap bmm WHERE bmm.board.id = :boardId AND bmm.member.email = :memberEmail")
    Optional<BoardMemberMap> findByBoardIdAndMemberEmail(@Param("boardId") Long boardId, @Param("memberEmail") String memberEmail);
}
