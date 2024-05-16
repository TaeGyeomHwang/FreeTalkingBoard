package com.bamboo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchDto {

    private String SearchBy;

    private String SearchQuery = "";
}
