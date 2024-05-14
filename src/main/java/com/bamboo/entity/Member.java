package com.bamboo.entity;

import com.bamboo.constant.Role;
import com.bamboo.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_email", unique = true)
    private String email;

    @Column(name = "member_name")
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "member_is_deleted")
    private boolean isDeleted;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        member.setName(memberFormDto.getName());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
}
