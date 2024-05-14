package com.bamboo.controller;

import com.bamboo.dto.ReplyDto;
import com.bamboo.dto.ReplyFormDto;
import com.bamboo.entity.Member;
import com.bamboo.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
public class ThymeleafExController {

    private final ReplyService replyService;

    //  댓글 컴포넌트 불러오기
    @GetMapping(value = "/test")
    public String test(Model model){
        List<ReplyDto> replyDtos = replyService.getReplyList(0L);
        model.addAttribute("replys", replyDtos);
        model.addAttribute("replyFormDto", new ReplyFormDto());
        return "thymeleafEx/test";
    }

    //  댓글 등록하기
    @PostMapping(value = "/new")
    public String replyNew(@Valid ReplyFormDto replyFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "thymeleafEx/test";
        }

        try{
            System.out.println("댓글 등록 시작");
            replyService.saveReply("test@test.com", 1L, replyFormDto);
        }catch (Exception e) {
            System.out.println("에러 메시지:"+e.getMessage());
            model.addAttribute("errorMessage", "댓글 등록 중 에러가 발생하였습니다.");
            return "thymeleafEx/test";
        }

        return "redirect:/comments/test";
    }
}
