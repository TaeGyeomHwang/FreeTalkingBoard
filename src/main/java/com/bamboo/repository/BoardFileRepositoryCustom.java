package com.bamboo.repository;

import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardFileRepositoryCustom {
    Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
}
