package com.bamboo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

//    private final ItemService itemService;

    @GetMapping(value = "/")
    public String Main(){

        return "main";
    }
}
