package com.bamboo.entity;

import com.bamboo.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name; // 유저 아이디

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
