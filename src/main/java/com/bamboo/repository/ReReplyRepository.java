package com.bamboo.repository;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReReplyRepository extends JpaRepository<ReReply, Long> {
    boolean existsByReply(Reply reply);

    @Query("SELECT rr FROM ReReply rr WHERE rr.isDeleted = false")
    List<ReReply> findAllNotDeleted();
}
