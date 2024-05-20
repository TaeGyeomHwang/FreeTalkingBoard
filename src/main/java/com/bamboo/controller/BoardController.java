package com.bamboo.controller;

import com.bamboo.dto.BoardFormDto;
import com.bamboo.entity.Board;
import com.bamboo.repository.BoardRepository;
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
                           @RequestParam("boardFile")List<MultipartFile> boardFileList){
        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        try{
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
        }catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 글입니다.");
            model.addAttribute("boardFormDto", new BoardFormDto());

            return "board/boardForm";
        }

        return "board/boardForm";
    }

    @PostMapping(value = "user/board/{boardId}")
    private String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
                               @RequestParam("boardFile") List<MultipartFile> boardFileList, Model model){

        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        try{
            boardService.updateBoard(boardFormDto, boardFileList);
        }catch (Exception e) {
            model.addAttribute("errorMessage", "글 수정 중 에러 발생");

            return "board/boardForm";
        }

        return "redirect:/";
    }

    @GetMapping("/user/boards")
    public String getBoardPage(Model model, Pageable pageable) {
        // 한 페이지에 보여줄 항목 수 설정
        pageable = PageRequest.of(0, 10); // 10개씩 페이지를 구성하도록 설정

        Page<Board> boardPage = boardRepository.findAll(pageable);
        model.addAttribute("boardPage", boardPage);

        return "main";
    }

    @GetMapping(value = "/board/{boardId}")
    public String boardDtl(Model model, @PathVariable("boardId") Long boardId){
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
        model.addAttribute("board", boardFormDto);

        return "board/boardDtl";
    }

//    @GetMapping(value = {"/user/boards", "/user/boards/{page}"})
//    public String boardMain(BoardSearchDto boardSearchDto, @PathVariable("page")Optional<Integer> page, Model model){
//
//        Pageable pageable = PageRequest.of(page.orElse(0),10);
//
//        Page<Board> boards = boardService.getBoardPage(boardSearchDto, pageable);
//        model.addAttribute("boards", boards);
//        model.addAttribute("boardSearchDto", boardSearchDto);
//        model.addAttribute("maxPage", 5);
//
//        return "main";
//    }
}
