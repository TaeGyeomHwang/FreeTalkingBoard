package com.bamboo.controller;

import com.bamboo.dto.BoardDto;
import com.bamboo.entity.Member;
import com.bamboo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data", "타임리프 예제");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model){

        BoardDto boardDto = new BoardDto();

        boardDto.setTitle("테스트 제목");
        boardDto.setContent("테스트 내용1");
        boardDto.setGood(10L);
        boardDto.setHit(1000L);
        boardDto.setRegTime(LocalDateTime.now());

        Member member = new Member();
        member.setName("작성자");

        boardDto.setMember(member.getName());

        model.addAttribute("boardDto", boardDto);


        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){

        List<BoardDto> boardDtoList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){

            BoardDto boardDto = new BoardDto();

            boardDto.setTitle("테스트 제목" + i);
            boardDto.setContent("테스트 내용1" + i);
            boardDto.setGood(10L);
            boardDto.setHit(1000L);
            boardDto.setRegTime(LocalDateTime.now());

            Member member = new Member();
            member.setName("작성자" + i);

            boardDto.setMember(member.getName());

            boardDtoList.add(boardDto);
        }

        model.addAttribute("boardDtoList", boardDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){

        List<BoardDto> boardDtoList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){

            BoardDto boardDto = new BoardDto();

            boardDto.setTitle("테스트 제목" + i);
            boardDto.setContent("테스트 내용1" + i);
            boardDto.setGood(10L);
            boardDto.setHit(1000L);
            boardDto.setRegTime(LocalDateTime.now());

            Member member = new Member();
            member.setName("작성자" + i);

            boardDto.setMember(member.getName());

            boardDtoList.add(boardDto);
        }

        model.addAttribute("boardDtoList", boardDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(Model model){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }

}