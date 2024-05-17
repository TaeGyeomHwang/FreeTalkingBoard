package com.bamboo.controller;

import com.bamboo.dto.BoardDto;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.service.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/board/new")
    public String boardForm(Model model) {
        model.addAttribute("boardFormDto",  new BoardFormDto());

        return "board/boardForm";
    }

    @PostMapping(value = "/board/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
                           @RequestParam("boardFile")List<MultipartFile> multipartFiles) {
        if(bindingResult.hasErrors()) {
            return "board/boardForm";
        }

        try {
            boardService.saveBoard(boardFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 작성중 에러 발생");

            return "board/boardForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/board/{boardId}")
    private String boardDtl(@PathVariable("boardId") Long itemId, Model model) {

        try{
            BoardFormDto boardFormDto = boardService.getBoardDtl(itemId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 글");
            model.addAttribute("boardFormDto", new BoardFormDto());

            return "board/boardForm";
        }

        return "board/boardForm";
    }

    @PostMapping(value = "/board/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, @RequestParam("boardFile") List<MultipartFile> multipartFiles, Model model){

        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        try {
            boardService.updateBoard(boardFormDto, multipartFiles);
        } catch (Exception e){
            model.addAttribute("errorMessage", "글 수정 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }
}
