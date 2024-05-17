package com.bamboo.service;

import com.bamboo.dto.BoardFileDto;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.entity.BoardFile;
import com.bamboo.repository.BoardFileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardFileService {

    @Value("${boardFileLocation}")
    private String boardFileLocation;

    private final BoardFileRepository boardFileRepository;

    private final FileService fileService;

    public void saveBoardFile(BoardFile boardFile, MultipartFile multipartFile) throws Exception {
        String oriFileName = multipartFile.getOriginalFilename();
        String fileName = "";
        String fileUrl = "";

        if(!StringUtils.isEmpty(oriFileName)){
            fileName = fileService.uploadFile(boardFileLocation, oriFileName, multipartFile.getBytes());
            fileUrl = "/files/item/" + fileName;
        }

        boardFile.updateBordFile(oriFileName, fileName, fileUrl);
        boardFileRepository.save(boardFile);
    }


    public void updateBoardFile(Long boardFileId, MultipartFile multipartFile) throws Exception{
        if(!multipartFile.isEmpty()){
            BoardFile savedBoardFile = boardFileRepository.findById(boardFileId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedBoardFile.getFileName())) {
                fileService.deleteFile(boardFileLocation+"/"+
                        savedBoardFile.getFileName());
            }

            String oriFileName = multipartFile.getOriginalFilename();
            String fileName = fileService.uploadFile(boardFileLocation, oriFileName, multipartFile.getBytes());
            String fileUrl = "/images/board/" + fileName;
            savedBoardFile.updateBordFile(oriFileName, fileName, fileUrl);
        }
    }

}
