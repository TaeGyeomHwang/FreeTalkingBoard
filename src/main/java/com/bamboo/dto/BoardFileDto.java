package com.bamboo.dto;

import com.bamboo.entity.BoardFile;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardFileDto {

    private Long id;

    private String fileName;

    private String oriFileName;

    private String fileUrl; //파일 경로

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardFileDto of(BoardFile boardFile){
        return modelMapper.map(boardFile, BoardFileDto.class);
    }
}
