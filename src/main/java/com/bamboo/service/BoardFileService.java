package com.bamboo.service;

import com.bamboo.entity.BoardFile;
import com.bamboo.repository.BoardFileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

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

        // 파일 업로드
        if (!StringUtils.isEmpty(oriFileName)) {
            try {
                fileName = fileService.uploadFile(boardFileLocation, oriFileName, multipartFile.getBytes());
                fileUrl = boardFileLocation + "/" + fileName;
                System.out.println("File uploaded successfully: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error uploading file: " + oriFileName);
                throw e;
            }
        }

        // 파일 정보 업데이트 및 저장
        try {
            boardFile.updateBoardFile(oriFileName, fileName, fileUrl);
            boardFileRepository.save(boardFile);
            System.out.println("BoardFile saved successfully in database: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving BoardFile in database: " + oriFileName);
            throw e;
        }
    }

    public void updateBoardFile(Long boardFileId, MultipartFile file) throws Exception {
        BoardFile boardFile = boardFileRepository.findById(boardFileId)
                .orElseThrow(EntityNotFoundException::new);

        // 기존 파일 삭제
        if (!StringUtils.isEmpty(boardFile.getFileName())) {
            fileService.deleteFile(boardFileLocation + "/" + boardFile.getFileName());
        }

        // 새로운 파일 업로드
        String oriFileName = file.getOriginalFilename();
        String fileName = fileService.uploadFile(boardFileLocation, oriFileName, file.getBytes());
        String fileUrl = boardFileLocation + "/" + fileName;

        // 파일 정보 업데이트
        boardFile.setOriFileName(oriFileName);
        boardFile.setFileName(fileName);
        boardFile.setFileUrl(fileUrl);

        boardFileRepository.save(boardFile);
    }

    public void deleteBoardFile(BoardFile boardFile) throws Exception {
        if (!StringUtils.isEmpty(boardFile.getFileName())) {
            fileService.deleteFile(boardFileLocation + "/" + boardFile.getFileName());
        }
        boardFileRepository.delete(boardFile);
    }

}
