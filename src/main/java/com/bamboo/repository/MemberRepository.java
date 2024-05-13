package com.bamboo.repository;

import com.bamboo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

//Member 엔티티를 데이터베이스에 저장할 수 있는 repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByEmail(String email);
}
