package com.bamboo.dto;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReplyFormDto {

    private String content;

    // 댓글 객체 생성
    public Reply createReply() {
        Reply reply = new Reply();
        reply.setContent(this.content);
        return reply;
    }

    // 대댓글 객체 생성
    public ReReply createReReply() {
        ReReply reReply = new ReReply();
        reReply.setContent(this.content);
        return reReply;
    }
}
