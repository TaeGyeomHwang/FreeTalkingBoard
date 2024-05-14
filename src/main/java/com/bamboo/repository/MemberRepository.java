package com.bamboo.repository;

import com.bamboo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByEmail(String email);
}
