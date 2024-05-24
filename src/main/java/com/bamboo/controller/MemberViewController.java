package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.MemberFormDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberViewController {
    @GetMapping("/login")
    public String login(Model model){


        model.addAttribute("MemberFormDto", new MemberFormDto());

        return "member/oauthLogin";
    }



    @GetMapping("/signup")
    public String signup(Model model){

        model.addAttribute("MemberFormDto", new MemberFormDto());

        return "member/signup";
    }

    @GetMapping("/modifyMember")
    public String modifyMember(Model model){

        model.addAttribute("MemberFormDto", new MemberFormDto());
        model.addAttribute("loginType", MyOAuth2MemberService.loginType);

        return "member/modifyMember";
    }
}
