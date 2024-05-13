package com.bamboo.controller;

import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/MemberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(MemberFormDto memberFormDto) {
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";
    }
}
