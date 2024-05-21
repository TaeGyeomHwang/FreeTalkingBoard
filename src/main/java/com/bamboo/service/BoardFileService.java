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
                fileUrl = "/files/board/" + fileName;
                System.out.println("File uploaded successfully: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error uploading file: " + oriFileName);
                throw e;
            }
        }

        // 파일 정보 업데이트 및 저장
        try {
            boardFile.updateBordFile(oriFileName, fileName, fileUrl);
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

        String oriFileName = file.getOriginalFilename();
        String fileName = fileService.uploadFile(boardFileLocation, oriFileName, file.getBytes());

        boardFile.setOriFileName(oriFileName);
        boardFile.setFileName(fileName);
        boardFile.setFileUrl(boardFileLocation + "/" + fileName);

        boardFileRepository.save(boardFile);
    }


}
