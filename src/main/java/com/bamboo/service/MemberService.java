package com.bamboo.service;

import com.bamboo.dto.MemberUpdateFormDto;
import com.bamboo.entity.Member;
import com.bamboo.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
/*@RequiredArgsConstructor*/
public class MemberService implements UserDetailsService {


    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


    //회원 수정
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public MemberUpdateFormDto getMemberDetail(String email) {
        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        return new MemberUpdateFormDto(member.getName(), "");
    }

    @Transactional
    public void updateMember(String email, MemberUpdateFormDto memberUpdateFormDto) {
        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("memberUpdateFormDto.getName(): "+memberUpdateFormDto.getName());
        member.setName(memberUpdateFormDto.getName());
        if (!memberUpdateFormDto.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(memberUpdateFormDto.getPassword()));
        }
        memberRepository.save(member);
    }

    // 회원 탈퇴 메서드
   /* public void withdrawMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException("회원을 찾을 수 없습니다.");
        }
        member.deleteMember(); // 회원 탈퇴 처리
    }*/

}
