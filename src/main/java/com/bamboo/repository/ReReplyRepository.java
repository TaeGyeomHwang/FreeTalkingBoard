package com.bamboo.repository;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReReplyRepository extends JpaRepository<ReReply, Long> {
    boolean existsByReply(Reply reply);
}
