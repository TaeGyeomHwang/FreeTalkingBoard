package com.bamboo.dto;

import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import com.bamboo.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardDto {

    private Long id;

    private String fileName;

    private String oriFileName;

    private String fileUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardDto of(BoardFile boardFile){
        return modelMapper.map(boardFile, BoardDto.class);
    }

}
