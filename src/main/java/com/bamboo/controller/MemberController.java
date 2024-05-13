package com.bamboo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    //회원가입 후 로그인 페이지로 이동
    @GetMapping(value = "/")
    public String login() {
        return "member/Login";
    }
}
