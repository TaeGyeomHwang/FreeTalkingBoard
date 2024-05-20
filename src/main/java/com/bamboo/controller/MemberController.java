package com.bamboo.controller;


import com.bamboo.dto.MemberFormDto;
import com.bamboo.dto.MemberUpdateFormDto;
import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {



    @Autowired
    private final MemberService memberService;
    @Autowired
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
    public String showUpdateForm(Principal principal, Model model) {
        String loginId = principal.getName();
        MemberUpdateFormDto memberUpdateFormDto = memberService.getMemberDetail(loginId);
        model.addAttribute("memberUpdateFormDto", memberUpdateFormDto);
        return "member/myPageForm";
    }

    //정보 수정
   @PostMapping(value = "/myPage")
    public String updateMember(Principal principal,
                               @Valid @ModelAttribute("memberUpdateFormDto") MemberUpdateFormDto memberUpdateFormDto,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/member/myPageForm";
        }
        String loginId = principal.getName();

        try {
            System.out.println("post 요청 전송");
            System.out.println("memberUpdateFormDto.getName(): "+memberUpdateFormDto.getName());
            memberService.updateMember(loginId, memberUpdateFormDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", "회원 정보 수정 중 오류가 발생했습니다.");
            return "/member/myPageForm";
        }

        return "redirect:/";
    }

    //회원 탈퇴
   /* @PostMapping("/withdraw")
    public String withdrawMember(Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        memberService.withdrawMember(email);
        redirectAttributes.addFlashAttribute("message", "탈퇴 되었습니다.");
        return "redirect:/";
    }*/

}
