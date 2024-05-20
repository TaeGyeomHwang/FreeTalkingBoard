package com.bamboo.repository;

import com.bamboo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    BoardFile findByBoardIdOrderByIdAsc(Long boardId);
}
