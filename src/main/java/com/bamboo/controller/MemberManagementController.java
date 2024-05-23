package com.bamboo.controller;


import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MemberManagementController {
    private final MemberService memberService;


    @GetMapping("/memberManagement")
    public String memberManagement(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "searchBy", required = false, defaultValue = "") String searchBy,
                                   @RequestParam(value = "searchQuery", required = false, defaultValue = "") String searchQuery,
                                   Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberService.getMemberPage(searchBy, searchQuery, pageable);

        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("currentPage", memberPage.getNumber());
        model.addAttribute("totalPages", memberPage.getTotalPages());
        model.addAttribute("totalMembers", memberPage.getTotalElements());
        model.addAttribute("searchBy", searchBy);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("maxPage", 10);

        return "admin/memberManagement";
    }
}
