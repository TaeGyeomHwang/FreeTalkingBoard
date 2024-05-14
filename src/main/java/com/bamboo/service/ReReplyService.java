package com.bamboo.service;

import com.bamboo.dto.ReplyFormDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.MemberRepository;
import com.bamboo.repository.ReReplyRepository;
import com.bamboo.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReReplyService {

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final ReReplyRepository reReplyRepository;

    //  대댓글 저장
    public Long saveReReply(String email, Long boardId, Long replyId, ReplyFormDto replyFormDto){
        ReReply reReply = replyFormDto.createReReply();

        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setMember(member);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setBoard(board);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setReply(reply);

        reReplyRepository.save(reReply);

        return reReply.getId();
    }

    //  대댓글 삭제
    public void cancelReReply(Long reReplyId){
        ReReply reReply = reReplyRepository.findById(reReplyId)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setDeleted(true);
    }
}
