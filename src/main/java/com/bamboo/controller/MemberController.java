package com.bamboo.controller;


import com.bamboo.dto.MemberFormDto;
import com.bamboo.dto.MemberUpdateDto;
import com.bamboo.entity.Member;
import com.bamboo.repository.MemberRepository;
import com.bamboo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMemberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }

    //내정보
    @GetMapping(value = "/myPage")
    public String updateMemberForm(Principal principal, Model model) {
        String loginId = principal.getName();
        Member member = memberRepository.findByEmail(loginId);
        model.addAttribute("member", member);
        return "/member/myPageForm";
    }

   @PostMapping(value = "/myPage")
    public String updateMember(@Valid MemberUpdateDto memberUpdateDto, Model model) {
        model.addAttribute("member", memberUpdateDto);
        memberService.updateMember(memberUpdateDto);

        return "redirect:/members/myPageForm";
    }

}
