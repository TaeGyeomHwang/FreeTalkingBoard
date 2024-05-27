package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.service.BoardServiceHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final BoardServiceHwang boardServiceHwang;

    //  게시글 등록 페이지
    @GetMapping(value = "/user/board/new")
    public String boardForm(Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardForm";
    }

    //  게시글 등록 요청
    @PostMapping(value = "/user/board/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
                           @RequestParam("boardFile") List<MultipartFile> boardFiles) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }

        try {
            //  카카오 로그인일 경우
            if (MyOAuth2MemberService.loginType == null) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                String email = authentication.getName();
                boardServiceHwang.saveBoard(email, boardFormDto, boardFiles);
            } else {
                //  일반 로그인일 경우
                String email = MyOAuth2MemberService.userEmail;
                boardServiceHwang.saveBoard(email, boardFormDto, boardFiles);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    //  게시글 수정 페이지
    @GetMapping(value = "/user/board/{boardId}")
    public String boardForm(@PathVariable("boardId") Long boardId, Model model) {
        try {
            BoardFormDto boardFormDto = boardServiceHwang.getBoardForm(boardId); // 수정 페이지에 필요한 데이터를 가져오기 위해 서비스 호출
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "없는 글입니다");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "board/boardForm";
        }

        return "board/boardForm";
    }

    //  게시글 수정 요청
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
            boardServiceHwang.updateBoard(boardFormDto, boardFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 수정 중 에러: " + e.getMessage());
            return "board/boardForm";
        }
        return "redirect:/";
    }
}
