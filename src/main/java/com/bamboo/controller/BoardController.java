//package com.boot.bambootest.controller;
//
//import com.boot.bambootest.dto.BoardFormDto;
//import com.boot.bambootest.dto.BoardSearchDto;
//import com.boot.bambootest.entity.Board;
//import com.boot.bambootest.repository.BoardRepository;
//import com.boot.bambootest.service.BoardService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequiredArgsConstructor
//public class BoardController {
//
//    private final BoardService boardService;
//    private final BoardRepository boardRepository;
//
//    @GetMapping(value = "/user/board/new")
//    public String boardForm(Model model) {
//        model.addAttribute("boardFormDto", new BoardFormDto());
//        return "board/boardForm";
//    }
//
//    @PostMapping(value = "/user/board/new")
//    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
//                           @RequestParam("boardFile") List<MultipartFile> boardFiles) {
//        if (bindingResult.hasErrors()) {
//            return "board/boardForm";
//        }
//
//        try {
//            boardService.saveBoard(boardFormDto, boardFiles);
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error during file upload: " + e.getMessage());
//            return "board/boardForm";
//        }
//
//        return "redirect:/";
//    }
//
//    @GetMapping(value = "/user/board/{boardId}")
//    public String boardDtl(@PathVariable("boardId") Long boardId, Model model) {
//        try {
//            // 조회수 증가를 위해 게시글 조회
//            boardService.getBoardHit(boardId);
//            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId); // 수정 페이지에 필요한 데이터를 가져오기 위해 서비스 호출
//            model.addAttribute("boardFormDto", boardFormDto);
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("errorMessage", "없는 글입니다");
//            model.addAttribute("boardFormDto", new BoardFormDto());
//            return "board/boardForm";
//        }
//
//        return "board/boardForm";
//    }
//
//    @PostMapping(value = "/user/board/update/{boardId}")
//    public String boardUpdate(@PathVariable("boardId") Long boardId,
//                              @Valid BoardFormDto boardFormDto,
//                              BindingResult bindingResult,
//                              Model model,
//                              @RequestParam("boardFile") List<MultipartFile> boardFileList) {
//        if (bindingResult.hasErrors()) {
//            return "board/boardForm";
//        }
//        try {
//            boardService.updateBoard(boardFormDto, boardFileList);
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "글 수정 중 에러: " + e.getMessage());
//            return "board/boardForm";
//        }
//        return "redirect:/";
//    }
//
//    @GetMapping(value = {"/", "/user/boards", "/user/boards/{page}"})
//    public String boardManage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
//        Pageable pageable = PageRequest.of(page.orElse(0), 5);
//        Page<Board> boards = boardService.getBoardPage(boardSearchDto, pageable);
//        model.addAttribute("boards", boards);
//        model.addAttribute("boardSearchDto", boardSearchDto);
//        model.addAttribute("maxPage", 10);
//
//        return "main";
//    }
//
//    @GetMapping(value = "/board/{boardId}")
//    public String boardDtl(Model model, @PathVariable("boardId") Long boardId) {
//        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
//        boardService.getBoardHit(boardId);
//        model.addAttribute("board", boardFormDto);
//        return "board/boardDtl";
//    }
//
//    @PostMapping(value = "/user/board/delete/{boardId}")
//    public String deleteBoard(@PathVariable("boardId") Long boardId) {
//        boardService.deleteBoard(boardId);
//        return "redirect:/";
//    }
//}

package com.bamboo.controller;

import com.bamboo.dto.BoardFormDto;
import com.bamboo.dto.BoardSearchDto;
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
import java.util.Optional;

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
                           @RequestParam("boardFile") List<MultipartFile> boardFiles) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }

        try {
            boardService.saveBoard(boardFormDto, boardFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error during file upload: " + e.getMessage());
            return "board/boardForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/user/board/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model) {
        try {
            // 조회수 증가를 위해 게시글 조회
            Board board = boardService.getBoardHit(boardId);
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId); // 수정 페이지에 필요한 데이터를 가져오기 위해 서비스 호출
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "없는 글입니다");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "board/boardForm";
        }

        return "board/boardForm";
    }

    @PostMapping(value = "/user/board/update/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Long boardId,
                              @Valid BoardFormDto boardFormDto,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam("boardFile") List<MultipartFile> boardFileList) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        try {
            boardService.updateBoard(boardFormDto, boardFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 수정 중 에러: " + e.getMessage());
            return "board/boardForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/", "/user/boards", "/user/boards/{page}"})
    public String boardManage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        Page<Board> boards = boardService.getBoardPage(boardSearchDto, pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 10);

        return "main";
    }

    @GetMapping(value = "/board/{boardId}")
    public String boardDtl(Model model, @PathVariable("boardId") Long boardId) {
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
        boardService.getBoardHit(boardId);
        model.addAttribute("board", boardFormDto);
        return "board/boardDtl";
    }

    @PostMapping(value = "/user/board/delete/{boardId}")
    public String deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/";
    }
}
