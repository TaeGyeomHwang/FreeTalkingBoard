package com.bamboo.entity;

import com.bamboo.constant.Role;
import com.bamboo.dto.MemberFormDto;
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
}
