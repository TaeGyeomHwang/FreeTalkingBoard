package com.bamboo.dto;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReplyFormDto {

    private Long id;

    private String content;

    private static ModelMapper modelMapper = new ModelMapper();

    public Reply createReply(){return modelMapper.map(this, Reply.class);}

    public ReReply createReReply(){return modelMapper.map(this, ReReply.class);}

}
