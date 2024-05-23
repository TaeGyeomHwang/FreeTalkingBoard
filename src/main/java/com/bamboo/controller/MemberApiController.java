package com.bamboo.controller;



import com.bamboo.dto.MemberDeleteDto;
import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.bamboo.config.oauth.MyOAuth2MemberService.loginType;

@RequiredArgsConstructor
@Controller
public class MemberApiController {
    private final MemberService memberService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    @PostMapping("/user")
    public String signup(@Valid @ModelAttribute("MemberFormDto") MemberFormDto request, BindingResult bindingResult) {

        // 이메일 형식이 올바른지 확인
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            bindingResult.rejectValue("email", "emailInCorrect", "유효한 이메일 주소를 입력해주세요.");

        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 16) {
            bindingResult.rejectValue("password", "passwordLengthIncorrect", "비밀번호는 8자 이상 16자 이하이어야 합니다.");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwordMismatch", "2개의 패스워드가 일치하지 않습니다.");
        }
        if (request.getName().length() < 3 || request.getName().length() > 8 ) {
            bindingResult.rejectValue("name", "nameLengthIncorrect", "이름은 3자 이상 8자 이하이어야 합니다.");
        }

        // 오류가 있으면 폼으로 되돌아감
        if (bindingResult.hasErrors()) {
            return "member/signup"; // 폼 페이지로 돌아가서 오류 메시지 표시
        }

        memberService.save(request); // 회원가입 메소드 호출

        return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody MemberFormDto request) {
        boolean checkEmail = memberService.findByEmail(request.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (!checkEmail) {
            response.put("message", "사용 가능한 이메일입니다.");
            response.put("check", true);
        } else {
            response.put("message", "사용 불가능한 이메일입니다.");
            response.put("check", false);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @PutMapping("/deleteMember")
    public ResponseEntity<Member> deleteMember(@RequestBody MemberDeleteDto request){
        Member updatedMember = memberService.updatedDelete(request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(updatedMember);

    }
    @PostMapping("/modifyMember")
    public String modifyMember(@ModelAttribute("MemberFormDto") MemberFormDto request, BindingResult bindingResult,
    RedirectAttributes redirectAttributes) {

        System.out.println(request.getEmail()+"이게 이메일이야 쟁재뱡ㅈㅂ얒버애ㅑㅂㅈ애ㅑㅈ배ㅓㅈ뱌ㅐ어ㅑ잽어ㅑㅐㅈ버ㅐㅑㅈ버ㅐㅑ저ㅑㅐ저ㅑㅐ");
        // 유효성 검사
        if (request.getName().length() < 3 || request.getName().length() > 8) {
            bindingResult.rejectValue("name", "nameLengthIncorrect", "이름은 3자 이상 8자 이하이어야 합니다.");
        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 16) {
            bindingResult.rejectValue("password", "passwordLengthIncorrect", "비밀번호는 8자 이상 16자 이하이어야 합니다.");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwordMismatch", "2개의 패스워드가 일치하지 않습니다.");
        }
        // 오류가 있으면 폼으로 되돌아감
        if (bindingResult.hasErrors()) {
            return "member/modifyMember"; // 폼 페이지로 돌아가서 오류 메시지 표시
        }
        memberService.modifyMember(request.getName(), request.getPassword(), request.getEmail());

        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/modifyMember"; // 로그인 페이지로 리다이렉트
    }
}


