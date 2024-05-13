package com.bamboo.service;

import com.bamboo.dto.ReplyFormDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
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
public class ReplyService {

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final ReReplyRepository reReplyRepository;

    public Long saveReply(String email, Long boardId, ReplyFormDto replyFormDto){
        Reply reply = replyFormDto.createReply();

        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        reply.setMember(member);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        reply.setBoard(board);

        replyRepository.save(reply);

        return reply.getId();
    }

    public void cancelReply(Long replyId){
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);
        reply.setDeleted(true);
    }

    public boolean isDeletedAndHasReReply(Long replyId){
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);

        if (reply.isDeleted()) {
            // 대댓글 테이블에서 사용중이라면
            return reReplyRepository.existsByReply(reply);
        } else {
            // 삭제되지 않았다면
            return false;
        }
    }
}
