package com.bamboo.controller;

import com.bamboo.dto.BoardFormDto;
import com.bamboo.repository.BoardRepository;
import com.bamboo.service.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping(value = "/user/board/new")
    public String boardForm(Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardForm";
    }

    @PostMapping(value = "/user/board/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
                           @RequestParam("boardFile") List<MultipartFile> boardFileList) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }

        try {
            boardService.saveBoard(boardFormDto, boardFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 등록 중 에러");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/user/board/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model) {

        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "없는 글입니다");
            model.addAttribute("boardFormDto", new BoardFormDto());

            return "board/boardForm";
        }

        return "board/boardForm";
    }
}
