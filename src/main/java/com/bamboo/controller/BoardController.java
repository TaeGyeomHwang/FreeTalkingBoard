package com.bamboo.controller;

import com.bamboo.dto.BoardFormDto;
import com.bamboo.dto.UpdateFileAllowedRequest;
import com.bamboo.service.BoardService;
import com.bamboo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final FileService fileService;

    @GetMapping("/boards/new_board")
    public String boards(Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());

        UpdateFileAllowedRequest updateFileAllowedRequest = fileService.getFileConfig(1L);
        model.addAttribute("updateFileAllowedRequest", updateFileAllowedRequest);
        return "board/newBoard";
    }

    @PostMapping("/boards/new_board")
    public String createBoard(BoardFormDto boardFormDto, @RequestParam("word") List<String>hashtags, MultipartFile[] uploadFiles) {

        //파일 업로드
        for (MultipartFile uploadFile : uploadFiles) {
            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName =uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            System.out.println("fileName: " + fileName);
            System.out.println("fileSize: " + uploadFile.getSize());
        }

        //해시태그
        System.out.println("hashtag이다1" + hashtags);

        boardService.saveBoard(boardFormDto, hashtags);
        return "redirect:/boards/new_board";
    }
}
