package com.bamboo.repository;


import com.bamboo.entity.Board;
import com.bamboo.entity.BoardHashtagMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardHashtagMapRepository extends JpaRepository<BoardHashtagMap, Long> {

    List<BoardHashtagMap> findByBoard(Board board);

    void deleteByBoard(Board board);

}
