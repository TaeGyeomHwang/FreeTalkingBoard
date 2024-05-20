package com.bamboo.controller;

import com.bamboo.dto.BoardSearchDto;
import com.bamboo.dto.MainDto;
import com.bamboo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;

    @GetMapping(value = {"/"})
    public String boardMain(BoardSearchDto boardSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.orElse(0),10);

        Page<MainDto> boardsDto = boardService.getMainPage(boardSearchDto, pageable);
        model.addAttribute("boards", boardsDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }
}
