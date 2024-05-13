package com.bamboo.repository;

import com.bamboo.constant.Role;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.entity.Reply;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ReplyRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ReplyRepository replyRepository;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setName("홍길동");
        String password = passwordEncoder.encode("1234");
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return memberRepository.save(member);
    }

    public Board createBoard(){
        Member member = createMember();

        Board board = new Board();
        board.setMember(member);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setDeleted(false);
        board.setRestored(false);
        board.setGood(0L);
        board.setHit(0L);

        return boardRepository.save(board);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createReplyTest(){
        Member member = createMember();
        Board board = createBoard();

        Reply reply = new Reply();
        reply.setContent("테스트 댓글");
        reply.setMember(member);
        reply.setBoard(board);
        reply.setDeleted(false);

        replyRepository.save(reply);

        em.flush();
        em.clear();

        Reply savedReply = replyRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(member.getEmail(), savedReply.getMember().getEmail());
    }

}