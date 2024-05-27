package com.bamboo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFormDto {

    private Long id;

    @NotBlank(message = "제목을 꼭 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 꼭 입력해주세요")
    private String content;

}
