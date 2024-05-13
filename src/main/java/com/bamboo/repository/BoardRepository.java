package com.bamboo.repository;

import com.bamboo.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByMemberEmail(String memberEmail);
}
