package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import com.bamboo.service.BoardServiceHwang;
import com.bamboo.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardServiceHwang boardServiceHwang;
    private final VisitService visitService;
    //  글 목록 조회(메인페이지)
    @GetMapping(value = {"/", "/{page}"})
    public String boards(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page,
                         @RequestParam(value = "sort", required = false) String sort,
                         Model model) {
        Pageable pageable;
        Page<Board> boards;


        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();
        Boolean loginType2 = (principal instanceof OAuth2User);

        System.out.println("로그인 타입은????????????????????????????????????????????"+loginType2);

        if (sort != null && !sort.isEmpty()) {
            pageable = PageRequest.of(page.orElse(0), 10, Sort.by(sort));
            boards = boardServiceHwang.getSortedBoardPage(boardSearchDto, pageable, sort);
        } else {
            pageable = PageRequest.of(page.orElse(0), 10);
            boards = boardServiceHwang.getBoardPage(boardSearchDto, pageable);
        }

        visitService.countVisit();

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 10);
        model.addAttribute("loginType", loginType2);

        return "board/boards";
    }

}
